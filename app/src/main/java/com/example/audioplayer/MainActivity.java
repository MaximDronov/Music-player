package com.example.audioplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Song selectedSong;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();

        ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song("ANNA ASTI - ЦАРИЦА", R.drawable.asti, R.raw.anna_asti_carica));
        songs.add(new Song("Баста - На заре («Альянс» Cover)", R.drawable.basta, R.raw.basta_na_zare));
        songs.add(new Song("Король И Шут - Кукла Колдуна", R.drawable.shut, R.raw.kukla_kolduna));
        songs.add(new Song("Мираж - Новый герой", R.drawable.geroy, R.raw.mirazh));
        songs.add(new Song("Винтаж, ТРАВМА, SKIDRI, DVRKLXGHT - Плохая Девочка", R.drawable.badgirl, R.raw.plohaya_devochka));
        // Добавьте остальные песни

        SongAdapter adapter = new SongAdapter(this, songs);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSong = songs.get(position);
                if (mediaPlayer != null) {
                    if (isPlaying) {
                        mediaPlayer.pause();
                        isPlaying = false;
                    } else {
                        try {
                            mediaPlayer.reset();
                            mediaPlayer = MediaPlayer.create(MainActivity.this, selectedSong.getAudioResource());
                            mediaPlayer.start();
                            seekBar.setMax(mediaPlayer.getDuration());
                            isPlaying = true;
                            updateSeekBar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Button playButton = findViewById(R.id.play_button);
        Button pauseButton = findViewById(R.id.pause_button);
        Button stopButton = findViewById(R.id.stop_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSong != null) {
                    if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                        try {
                            if (mediaPlayer == null) {
                                mediaPlayer = MediaPlayer.create(MainActivity.this, selectedSong.getAudioResource());
                                seekBar.setMax(mediaPlayer.getDuration());
                            }
                            mediaPlayer.seekTo(seekBar.getProgress());
                            mediaPlayer.start();
                            isPlaying = true;
                            updateSeekBar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Выберите песню для воспроизведения", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPlaying = false;
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlaying = false;
                    seekBar.setProgress(0);
                }
            }
        });
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && isPlaying) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.isPlaying()) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        updateSeekBar();
                    }
                };
                seekBar.postDelayed(runnable, 1000);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
