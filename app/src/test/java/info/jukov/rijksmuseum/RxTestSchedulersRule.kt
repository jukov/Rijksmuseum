package info.jukov.rijksmuseum

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RxTestSchedulersRule : TestWatcher() {

    val scheduler = TestScheduler()

    override fun starting(description: Description?) {
        RxJavaPlugins.setIoSchedulerHandler {
            scheduler
        }
        RxJavaPlugins.setComputationSchedulerHandler {
            scheduler
        }
        RxAndroidPlugins.setMainThreadSchedulerHandler {
            scheduler
        }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            scheduler
        }
    }

    override fun finished(description: Description?) {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}