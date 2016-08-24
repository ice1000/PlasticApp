package utils

/**
 * Created by asus1 on 2016/8/24.
 * @author ice1000
 */

inline fun loop(block: () -> Unit) {
    while (true) block.invoke()
}

inline fun forceRun(block: () -> Unit) {
    try {
        block.invoke()
    } catch (e: Throwable) {
    }
}
