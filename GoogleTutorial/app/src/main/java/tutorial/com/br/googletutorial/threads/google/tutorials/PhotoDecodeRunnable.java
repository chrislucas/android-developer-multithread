package tutorial.com.br.googletutorial.threads.google.tutorials;

import android.os.Process;

/**
 * Created by C.Lucas on 28/10/2015.
 *
 * Definindo uma classe capaz de executar uma tarefe em uma Thread
 * diferente da Thread que controla a UI
 * Esse tipo de classe eh conhecido como Task, pois inicia uma tarefa
 * Numa Thread separada possibilitando que uma funcionalidade seja executada
 * em concorrencia com outra(s)
 *
 */
public class PhotoDecodeRunnable implements Runnable {

    final TaskRunnableDecodeMethods photoTask;

    interface TaskRunnableDecodeMethods {
        public void setImageDecodeThread(Thread thread);
    }

    public PhotoDecodeRunnable(TaskRunnableDecodeMethods photoTask) {
        this.photoTask = photoTask;
    }

    @Override
    public void run() {
        // definindo a prioridade da thread para rodar em background com THREAD_PRIORITY_BACKGROUND
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        // guardar a referencia da Thread atual na referencia da class PhotoTast
        // assim tal referencia tera o poder de interromper a Thread
        photoTask.setImageDecodeThread(Thread.currentThread());
    }
}
