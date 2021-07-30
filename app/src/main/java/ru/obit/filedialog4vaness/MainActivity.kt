package ru.obit.filedialog4vaness

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<Button>(R.id.btOpenFD).setOnClickListener{
            startSelectFiles()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun startSelectFiles() {

       val intent = Intent()
           .setAction("ru.obit.FileDialog4Vaness.action.SelectFiles")

        startActivityForResult(Intent.createChooser(intent, "Select files"), 102)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 102 && resultCode == RESULT_OK && null != data) {

            if (data.hasExtra("path")) {
                findViewById<TextView>(R.id.etSelectedFiles).text =
                    data.extras?.get("path").toString()
            }
        }
    }
}

class SelectFiles : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startSelectFiles()

    }

    private fun startSelectFiles() {

        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        startActivityForResult(Intent.createChooser(intent, "Select files"), 101)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == RESULT_OK && null != data) {
            val strOfFiles = StringBuilder()
            strOfFiles.append("[")
            if (null != data.clipData) { // checking multiple selection or not
                val itemsCount = data.clipData!!.itemCount
                for (i in 0 until itemsCount) {
                    val flUri = data.clipData?.getItemAt(i)?.uri
                    val flExt = contentResolver.getType(flUri!!.normalizeScheme())
                    val fname = getFileName(flUri!!.normalizeScheme())
                    strOfFiles.append("{\"uri\":\"")
                        .append(flUri.toString())
                        .append("\",\"type\":\"")
                        .append(flExt.toString())
                        .append("\",\"name\":\"")
                        .append(fname)
                        .append("\"}")
                    if (i != itemsCount - 1)
                        strOfFiles.append(",")
                }
            } else {
                val flExt = contentResolver.getType(data.data!!.normalizeScheme())
                val fname = getFileName(data.data!!.normalizeScheme())
                strOfFiles.append("{\"uri\":\"")
                    .append(data.data.toString())
                    .append("\",\"type\":\"")
                    .append(flExt.toString())
                    .append("\",\"name\":\"")
                    .append(fname)
                    .append("\"}")

            }

            val intent = Intent()
            strOfFiles.append("]")
            intent.putExtra("path", strOfFiles.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        else {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    result =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1)
                result = result!!.substring(cut + 1)
        }
        return result
    }

}

class AppExist : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(Activity.RESULT_OK, intent)
        finish()

    }
}