package br.edu.ifsp.scl.ads.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.ads.intents.MainActivity.Constantes.PARAMETRO_EXTRA
import br.edu.ifsp.scl.ads.intents.databinding.ActivityParameterBinding

class ParameterActivity : AppCompatActivity() {
    private val binding: ActivityParameterBinding by lazy {
        ActivityParameterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra(PARAMETRO_EXTRA)?.let { parametro ->
            binding.parameterTxt.setText(parametro)
        }

        binding.sendParamBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra(PARAMETRO_EXTRA, binding.parameterTxt.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}