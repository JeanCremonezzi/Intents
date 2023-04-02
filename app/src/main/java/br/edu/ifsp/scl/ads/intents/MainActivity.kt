package br.edu.ifsp.scl.ads.intents

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
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
    private lateinit var callPermissionArl: ActivityResultLauncher<String>
    private lateinit var getImageArl: ActivityResultLauncher<Intent>

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
            val intent = Intent("PARAM_ACTION")
            intent.putExtra("PARAMETRO_EXTRA", binding.paramTxt.text.toString())

            parl.launch(intent)
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ permissaoConcedida ->
            if (permissaoConcedida) callNumber(true)
            else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        getImageArl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    binding.paramTxt.text = it.toString()
                    val toViewImage = Intent(ACTION_VIEW, it)
                    startActivity(toViewImage)
                }
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED) callNumber(true)
                    else callPermissionArl.launch(CALL_PHONE)
                } else callNumber(true)
                true
            }

            R.id.dialMi -> {
                callNumber(false)
                true
            }

            R.id.pickMi -> {
                val getImageIntent = Intent(ACTION_PICK)
                val imageDirectory = Environment
                    .getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
                    .path

                getImageIntent.setDataAndType(Uri.parse(imageDirectory), "image/*")
                getImageArl.launch(getImageIntent)
                true
            }

            R.id.chooserMi -> {
                val url: Uri = Uri.parse(binding.paramTxt.text.toString())
                val browserIntent = Intent(ACTION_VIEW, url)

                val selectAppIntent = Intent(ACTION_CHOOSER)
                selectAppIntent.putExtra(EXTRA_TITLE, "Choose a browser")
                selectAppIntent.putExtra(EXTRA_INTENT, browserIntent)

                startActivity(selectAppIntent)
                true
            }

            else ->false
        }
    }

    private fun callNumber (call: Boolean) {
        val numberUri: Uri = Uri.parse("tel: ${binding.paramTxt.text}")
        val callIntent = Intent(if (call) ACTION_CALL else ACTION_DIAL)
        callIntent.data = numberUri
        startActivity(callIntent)
    }
}