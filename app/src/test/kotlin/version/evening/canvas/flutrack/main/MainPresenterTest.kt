package version.evening.canvas.flutrack.main

import android.os.Bundle
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

class MainPresenterTest {
    @Mock
    private lateinit var flutrackAll: FlutrackAll
    @Mock
    private lateinit var storage: MemoryFlutweetsStorage
    @Mock
    private lateinit var schedulers: SchedulersWrapper
    @Mock
    private lateinit var view: MainContract.View

    private lateinit var presenter: MainContract.Presenter

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())

        presenter = MainPresenter(flutrackAll, storage, schedulers, view)
    }

    @Test
    fun testSaveState() {
        presenter.saveState(Bundle())

        verify(view).dismissErrorDialog()
    }

    @Test
    fun testActionAbout() {
        presenter.actionAbout()

        verify(view).showAboutDialog()
    }
}
