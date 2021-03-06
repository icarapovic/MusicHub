package dev.chapz.musichub

import android.app.Application
import com.google.android.material.color.DynamicColors
import dev.chapz.musichub.repository.*
import dev.chapz.musichub.service.MediaServiceConnection
import dev.chapz.musichub.ui.HostViewModel
import dev.chapz.musichub.ui.albums.AlbumViewModel
import dev.chapz.musichub.ui.songs.SongViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MusicApp : Application() {

    private val repositoryModule = module {
        single<SongRepository> { SongRepositoryImpl(androidContext().contentResolver) }
        single<AlbumRepository> { AlbumRepositoryImpl(androidContext().contentResolver) }
    }

    private val mediaModule = module {
        single { MediaServiceConnection(androidContext()) }
        single { MediaManager() }
    }

    private val viewModelModule = module {
        viewModel { HostViewModel(get()) }
        viewModel { SongViewModel(get()) }
        viewModel { AlbumViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        startKoin {
            androidLogger()
            androidContext(this@MusicApp)
            modules(repositoryModule, mediaModule, viewModelModule)
        }
    }
}