package br.edu.ifsp.scl.ads.intents

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.scl.ads.intents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var  parl : ActivityResultLauncher<Intent>
    private lateinit var  activityResultLauncher: ActivityResultLauncher<String>

    companion object Constantes {
        const val PARAMETRO_EXTRA = "PARAMETRO_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                val retorno = result.data?.getStringExtra(PARAMETRO_EXTRA)?:""
                binding.paramTxt.text = retorno
            }
        }

        binding.submitParamBtn.setOnClickListener {
            val intent = Intent("DIA_DE_SOL_ACTION")
            intent.putExtra("PARAMETRO_EXTRA", binding.paramTxt.text.toString())

            parl.launch(intent)
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ permissaoConcedida ->
            if (permissaoConcedida) {
                // TODO
            } else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.viewMi -> {
                val url: Uri = Uri.parse(binding.paramTxt.text.toString())
                val browserIntent: Intent = Intent(Intent.ACTION_VIEW, url)
                startActivity(browserIntent)

                true
            }

            R.id.callMi -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // TODO
                } else {
                    // TODO
                }

                true
            }

            R.id.dialMi -> true
            R.id.pickMi -> true
            R.id.chooserMi -> true
            else ->false
        }
    }
}