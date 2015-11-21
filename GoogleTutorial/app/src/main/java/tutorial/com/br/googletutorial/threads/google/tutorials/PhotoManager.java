package tutorial.com.br.googletutorial.threads.google.tutorials;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by C.Lucas on 28/10/2015.
 *
 * Se o programador necessita executar uma tarefa
 * varias vezes, com conjuntos de dados diferentes
 * e tal executção ocrre sequencialmente, uma por vez
 * O indicado eh o uso da classe IntentService
 *
 */
public class PhotoManager {

    static final int DOWNLOAD_FAILED    = -1;
    static final int DOWNLOAD_STARTED   = 1;
    static final int DOWNLOAD_COMPLETE  = 2;
    static final int DECODE_STARTED     = 3;
    static final int TASK_COMPLETE      = 4;

    private static PhotoManager instance = null;
    private Handler handler;

    /*
    * Para iniciar um Pool de Threads e necessario definir o numero
    * maximo de threads que eh possivel/oermitido alocar no pool
    * e o numero que sera alocado
    * Esse numero depende primeiramente do numero de CORES(nucleos de processamento) dp
    * dipositivo. Esse numero esta disponivel no ambiente do sistema
    * Nem sempre o valor retornado pela funcao availableProcessors e numero total de CORES
    * */
    private static final int  NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    /*
    * fila de tarefas
    * Uma fila de objetos do tipo Runnable
    * Um thread pool manager busca um objeto do tipo runnable
    * numa estrutura do tipo first in first out e adiciona a Thread
    * Criar uma Fila de qualquer tipo que implemente a interface BlockingQueue
    * */
    private final BlockingQueue<Runnable> decodeWorkQueue;
    // fila de runnables para download de imagens
    private final BlockingQueue<Runnable> downloadWorkQueue;
    // fila de runnables para PhotoManager.class
    private final Queue<PhotoTask> photoTaskWorkQueue;
    /**
     * Keep alive time and time Unit
     * Duracao que a thread permancece inativo(do ingles "idle") antes de ser desligada. Essa duracao
     * e mensurada por uma unidade de tempo (Time Unit), uma constante definida éla classe
     * TimeUnit
     * */
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int KEEP_ALIVE_TIME = 1;
    /**
     * ThreadPoolExecutor eh a classe que permitira criar um pool de threads
     * ao instanciar essa classe, criaremos e gerenciaremos um grupo restrito de threads
     * ThreadPoolExecutor criara todos os objetos thread quando instanciada
     * */
    private final ThreadPoolExecutor decodeThreadPoolExecutor, downloadThreadPoolExecutor;

    private PhotoManager() {
        decodeWorkQueue     = new LinkedBlockingQueue<Runnable>();
        downloadWorkQueue   = new LinkedBlockingQueue<Runnable>();
        photoTaskWorkQueue  = new LinkedBlockingQueue<PhotoTask>();

        decodeThreadPoolExecutor    = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, decodeWorkQueue);

        downloadThreadPoolExecutor  = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, decodeWorkQueue);

        // definir um handler que esta acoplado a UI Thread
        handler = new Handler(Looper.getMainLooper()) {

            /**
             * handleMessage() define uma operacao a ser realizada
             * quando Handler recebe uma nova mensagem para processar
             * */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

    }

    public static PhotoManager getInstance() {
        if(instance == null) {
            PhotoManager.instance = new PhotoManager();
        }
        return PhotoManager.instance;
    }
/*
    public static PhotoTask startDownload (PhotoView imageView, boolean cacheFlag) {
        getInstance();
    }
*/
    public void handleState(PhotoTask photoTask, int state) {
        switch (state) {
            case DOWNLOAD_COMPLETE:
                decodeThreadPoolExecutor.execute(photoTask.getPhotoDecodeRunnable());
                break;
            default:
                break;
        }
    }
}
