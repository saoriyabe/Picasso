package practice.saori.picasso;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import practice.saori.picasso.view.PicassoView;

public class MainActivity extends AppCompatActivity {

    private PicassoView picassoview;
    private AlertDialog.Builder currentAlertDialog;
    private ImageView changeWidthImageView;
    private View changeColorView;
    private AlertDialog dialog;
    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picassoview = findViewById(R.id.view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            default:
                break;
            case R.id.erase:
                picassoview.clear();
                break;
            case R.id.colorId:
                showColorChangeDialog();
                break;
            case R.id.lineWidth:
                showLineWidthDialog();
                break;
            case R.id.saveImage:
                picassoview.saveImage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showColorChangeDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.color_dialog, null);
        Button button = view.findViewById(R.id.setColorButton);
        changeColorView = view.findViewById(R.id.colorView);
        alphaSeekBar = view.findViewById(R.id.alphaSeekBar);
        redSeekBar = view.findViewById(R.id.redSeekBar);
        greenSeekBar = view.findViewById(R.id.greenSeekBar);
        blueSeekBar = view.findViewById(R.id.blueSeekBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(), greenSeekBar.getProgress(), blueSeekBar.getProgress());
                picassoview.setDrawingColor(color);
                dialog.dismiss();
                currentAlertDialog = null;
                //Log.d("onClick", "color:" + color);
            }
        });
        currentAlertDialog.setView(view);
        dialog = currentAlertDialog.create();
        dialog.setTitle("Set Color");
        dialog.show();
        alphaSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangeListener);

        int color = picassoview.getDrawingColor();
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        greenSeekBar.setProgress(Color.green(color));
        blueSeekBar.setProgress(Color.blue(color));
    }

    private SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            changeColorView.setBackgroundColor(
                    Color.argb(alphaSeekBar.getProgress(),
                            redSeekBar.getProgress(),
                            greenSeekBar.getProgress(),
                            blueSeekBar.getProgress())
            );
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public void showLineWidthDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.width_dialog, null);
        final SeekBar seekBar = view.findViewById(R.id.seekBar);
        Button button = view.findViewById(R.id.button);
        changeWidthImageView = view.findViewById(R.id.imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picassoview.setLineWidth(seekBar.getProgress());
                dialog.dismiss();
                currentAlertDialog = null;
            }
        });

        seekBar.setOnSeekBarChangeListener(widthSeekBarChange);
        seekBar.setProgress(picassoview.getLineWidth());
        currentAlertDialog.setView(view);
        dialog = currentAlertDialog.create();
        dialog.setTitle("Set Line Width");
        dialog.show();
    }

    private SeekBar.OnSeekBarChangeListener widthSeekBarChange = new SeekBar.OnSeekBarChangeListener() {
        Bitmap bitmap = Bitmap.createBitmap(400,100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Paint p = new Paint();
            p.setColor(picassoview.getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(progress);

            bitmap.eraseColor(Color.WHITE);
            canvas.drawLine(30,50,370,50,p);
            changeWidthImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
