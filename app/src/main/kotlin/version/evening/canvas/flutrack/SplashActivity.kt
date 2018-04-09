package version.evening.canvas.flutrack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import version.evening.canvas.flutrack.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, MainActivity::class.java).let { startActivity(it) }
        finish()
    }
}
