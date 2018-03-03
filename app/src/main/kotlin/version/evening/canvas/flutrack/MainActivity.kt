package version.evening.canvas.flutrack

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputModifier = InputModifier()

        process_button.setOnClickListener { output_field.text = inputModifier.modify(input_field.text.toString()) }
    }
}
