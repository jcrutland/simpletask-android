package nl.mpcjanssen.simpletask.util

import android.os.Handler
import android.os.Looper

object TodoActionQueue : Thread() {
    private var mHandler: Handler? = null
    private val TAG = "ActionQueue"
    init {
        start()
    }
    override fun run(): Unit {
        Looper.prepare()
        mHandler = Handler() // the Handler hooks up to the current Thread
        Looper.loop()
    }

    fun hasPending () : Boolean {
        return mHandler?.hasMessages(0) ?: false
    }

    fun add(description: String, r: Runnable, silent: Boolean = false) {
        while (mHandler == null) {
            if (!silent) {
                log.debug(TAG, "Queue handler is null, waiting")
            }
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        if (!silent) {
            mHandler?.post(LoggingRunnable("TLQ: $description", r))
        } else {
            mHandler?.post (r)
        }

    }
//    private fun buildStackTraceString(elements: Array<StackTraceElement>) : String {
//        val sb = StringBuilder();
//        elements.reverse()
//        elements.forEach {
//            sb.append("\n" + it.toString());
//        }
//        return sb.toString();
//    }
}

object FileStoreActionQueue : Thread() {
    private var mHandler: Handler? = null
    private val TAG = "ActionQueue"

    init {
        start()
    }

    override fun run(): Unit {
        Looper.prepare()
        mHandler = Handler() // the Handler hooks up to the current Thread
        Looper.loop()
    }

    fun hasPending(): Boolean {
        return mHandler?.hasMessages(0) ?: false
    }

    fun add(description: String, r: Runnable, silent: Boolean = false) {
        while (mHandler == null) {
            if (!silent) {
                log.debug(TAG, "Queue handler is null, waiting")
            }
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        if (!silent) {
            mHandler?.post(LoggingRunnable("FSQ: $description", r))
        } else {
            mHandler?.post(r)
        }

    }
//    private fun buildStackTraceString(elements: Array<StackTraceElement>) : String {
//        val sb = StringBuilder();
//        elements.reverse()
//        elements.forEach {
//            sb.append("\n" + it.toString());
//        }
//        return sb.toString();
//    }
}


class LoggingRunnable (val description: String, val runnable: Runnable) : Runnable {
    private val TAG = TodoActionQueue::class.java.simpleName

    override fun toString(): String {
        return description
    }

    override fun run() {
        log.info(TAG, description)
        runnable.run()
    }
}


