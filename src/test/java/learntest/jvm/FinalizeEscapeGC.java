package learntest.jvm;

public class FinalizeEscapeGC {
    public static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive(){
        System.out.println("我活着");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize 方法被调用");
        SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();

        SAVE_HOOK = null;
        System.gc();
        // finalize 方法优先级低,暂停 0.5 秒等待
        Thread.sleep(500);
        if(SAVE_HOOK == null){
            System.out.println("我死了");
        }else {
            SAVE_HOOK.isAlive();
        }


        SAVE_HOOK = null;
        System.gc();
        // finalize 方法优先级低,暂停 0.5 秒等待
        Thread.sleep(500);
        if(SAVE_HOOK == null){
            System.out.println("我死了");
        }else {
            SAVE_HOOK.isAlive();
        }
    }
}
