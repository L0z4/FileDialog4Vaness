package ru.obit.filedialog4vaness

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
            startSelectFiles();
        };

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
           .setAction("ru.obit.FileDialog4Vaness.action.SelectFiles");

        startActivityForResult(Intent.createChooser(intent, "Select files"), 102)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 102 && resultCode == RESULT_OK && null != data) {

            if (data.hasExtra("path")) {
                findViewById<TextView>(R.id.etSelectedFiles).text =
                    data.extras?.get("path").toString();
            }
        }
    }
}

class SelectFiles : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startSelectFiles();

    }

    private fun startSelectFiles() {

        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        startActivityForResult(Intent.createChooser(intent, "Select files"), 101)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == RESULT_OK && null != data) {
            var selectedFiles: Array<String>;

            if (null != data.clipData) { // checking multiple selection or not
                selectedFiles = Array(data.clipData!!.itemCount) { _ -> ""};
                for (i in 0 until data.clipData!!.itemCount) {
                    selectedFiles[i] = data.clipData?.getItemAt(i)?.uri.toString()
                }
            } else {
                selectedFiles = arrayOf(data.data.toString());
            }

            val intent = Intent()
            intent.putExtra("path", selectedFiles.joinToString(separator = "\n"))
            setResult(Activity.RESULT_OK, intent)
            finish();
        }
        else {
            setResult(Activity.RESULT_CANCELED, intent)
            finish();
        };
    }

}

class AppExist : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(Activity.RESULT_OK, intent)
        finish();

    }
}