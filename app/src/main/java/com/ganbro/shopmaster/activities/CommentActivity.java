package com.ganbro.shopmaster.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.adapters.CommentAdapter;
import com.ganbro.shopmaster.database.VideoDao;
import com.ganbro.shopmaster.models.Video;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private TextView videoDescription;
    private EditText editTextComment;
    private Button buttonSubmitComment;
    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private List<String> commentList;
    private Video video;
    private VideoDao videoDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        videoDescription = findViewById(R.id.video_description);
        editTextComment = findViewById(R.id.edit_text_comment);
        buttonSubmitComment = findViewById(R.id.button_submit_comment);
        recyclerViewComments = findViewById(R.id.recycler_view_comments);

        int videoId = getIntent().getIntExtra("VIDEO_ID", -1);
        videoDao = new VideoDao(this);
        video = videoDao.getVideoById(videoId);

        if (video != null) {
            videoDescription.setText(video.getDescription());
            commentList = video.getComments();
        } else {
            commentList = new ArrayList<>();
        }

        commentAdapter = new CommentAdapter(commentList);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(commentAdapter);

        buttonSubmitComment.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString();
            if (!commentText.isEmpty()) {
                video.addComment(commentText);
                videoDao.updateComments(video.getId(), video.getComments());
                commentAdapter.notifyDataSetChanged();
                editTextComment.setText("");
                recyclerViewComments.scrollToPosition(commentList.size() - 1);
            } else {
                Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
