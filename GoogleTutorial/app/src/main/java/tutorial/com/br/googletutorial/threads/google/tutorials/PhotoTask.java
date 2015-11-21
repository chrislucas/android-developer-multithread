package tutorial.com.br.googletutorial.threads.google.tutorials;

/**
 * Created by C.Lucas on 29/10/2015.
 */
public class PhotoTask implements PhotoDecodeRunnable.TaskRunnableDecodeMethods {

    private Runnable photoDecodeRunnable;

    public PhotoTask() {
        this.photoDecodeRunnable = new PhotoDecodeRunnable(this);
    }

    @Override
    public void setImageDecodeThread(Thread thread) {

    }

    public Runnable getPhotoDecodeRunnable() {
        return photoDecodeRunnable;
    }
}
