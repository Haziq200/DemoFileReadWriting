package sg.edu.rp.dmsd.demofilereadwriting;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    String folderLocation;
    Button btnwrite, btnread;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnwrite = findViewById(R.id.btnWrite);
        btnread = findViewById(R.id.btnRead);
        tv = findViewById(R.id.tv);

        int permissionCheck = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }


        //   if (!checkPermission()) {
        //       Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        //     ActivityCompat.requestPermissions(MainActivity.this,
        //              new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        //  }

        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Folder";

        File folder = new File(folderLocation);
        if (folder.exists() == false) {
            boolean result = folder.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "Folder created");
            }
        }


        btnwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission() == true) {
                    try {
                        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Folder";
                        File targetFile = new File(folderLocation, "text.txt");
                        FileWriter writer = new FileWriter(targetFile, true);
                        writer.write("My Name is Haziq\n");
                        writer.flush();
                        writer.close();
                        Toast.makeText(MainActivity.this, "File Created", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to write", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

        });

        btnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Folder";
                File newFile = new File(folderLocation, "text.txt");
                if (newFile.exists() == true) {
                    String data = "";
                    try {
                        FileReader reader = new FileReader(newFile);
                        BufferedReader br = new BufferedReader(reader);
                        String strline = br.readLine();
                        while (strline != null) {
                            data += strline + "\n";
                            strline = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Failed to display", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    Log.d("content", data);
                    tv.setText(data);
                }


            }
        });
    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}

