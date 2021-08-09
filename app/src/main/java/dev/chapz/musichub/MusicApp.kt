package dev.chapz.musichub

import android.app.Application
import dev.chapz.musichub.repository.SongRepository
import dev.chapz.musichub.repository.SongRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MusicApp : Application() {

    private val repositoryModule = module {
        single<SongRepository> { SongRepositoryImpl(androidContext().contentResolver) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MusicApp)
            modules(repositoryModule)
        }
    }
}