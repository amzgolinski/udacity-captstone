package com.amzgolinski.yara.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amzgolinski.yara.R;
import com.amzgolinski.yara.activity.SubmissionDetailActivity;
import com.amzgolinski.yara.data.RedditContract;
import com.amzgolinski.yara.ui.SubmissionListFragment;
import com.amzgolinski.yara.util.Utils;
import com.squareup.picasso.Picasso;


public class SubmissionsAdapter extends RecyclerView.Adapter<SubmissionsAdapter.ViewHolder> {

  private static final String LOG_TAG = SubmissionsAdapter.class.getName();

  // Store the context for easy access
  private Context mContext;
  private CursorAdapter mCursorAdapter;

  public SubmissionsAdapter(Context context, Cursor cursor) {

    mContext = context;
    mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {

      @Override
      public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.submission_item, parent, false);
      }

      @Override
      public void bindView(View view, Context context, Cursor cursor) {
        //Log.d(LOG_TAG, DatabaseUtils.dumpCursorToString(cursor));
        // subreddit name
        TextView submissionSubreddit = (TextView) view.findViewById(R.id.submission_item_subreddit);
        String subredditText = String.format(
            mContext.getResources().getString(R.string.subreddit_name),
            cursor.getString(SubmissionListFragment.COL_SUBREDDIT_NAME)
        );
        submissionSubreddit.setText(subredditText);

        // submission title
        TextView submissionTitle = (TextView) view.findViewById(R.id.submission_item_title);
        submissionTitle.setText(cursor.getString(SubmissionListFragment.COL_TITLE));


        // submission comment count
        TextView submissionCommentCount
            = (TextView) view.findViewById(R.id.submission_item_comments);
        String commentCount = String.format(
            mContext.getResources().getString(R.string.submission_comment_count),
            cursor.getInt(SubmissionListFragment.COL_COMMENT_COUNT)
        );
        submissionCommentCount.setText(commentCount);

        // submission score
        TextView submissionScore = (TextView) view.findViewById(R.id.submission_item_score);
        submissionScore.setText(cursor.getString(SubmissionListFragment.COL_SCORE));


        String thumbnail = cursor.getString(SubmissionListFragment.COL_THUMBNAIL);
        ImageView thumbnailView = (ImageView) view.findViewById(R.id.submission_item_thumbnail);
        if (!Utils.isStringEmpty(thumbnail)) {

          Picasso.with(context)
              .load(thumbnail)
              .error(R.drawable.ic_do_not_distrub_black_24dp)
              .into(thumbnailView);
        } else {
          thumbnailView.setImageDrawable(
              mContext.getResources().getDrawable(R.drawable.ic_comment_white_36dp, null)
          );
        }
      }
    };
  }

  public void swapCursor(Cursor cursor) {
    Log.d(LOG_TAG, "swapCursor");
    mCursorAdapter.swapCursor(cursor);
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    // Passing the binding operation to cursor loader
    mCursorAdapter.getCursor().moveToPosition(position);
    mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Passing the inflater job to the cursor-adapter
    View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
    return new ViewHolder(v);
  }

  @Override
  public int getItemCount() {
    //Log.d(LOG_TAG, "getItemCount: " + mCursorAdapter.getCount());
    return mCursorAdapter.getCount();
  }

  // Provide a direct reference to each of the views within a data item
  // Used to cache the views within the item layout for fast access
  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    TextView mSubmissionTitle;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ViewHolder(View itemView) {

      // Stores the itemView in a public final member variable that can be used
      // to access the context from any ViewHolder instance.
      super(itemView);
      itemView.setOnClickListener(this);
      mSubmissionTitle = (TextView) itemView.findViewById(R.id.subreddit_name);

    }

    public void onClick(View view) {
      int position = getAdapterPosition();

      Log.d(LOG_TAG, "onClick " + Integer.toString(position));
      Cursor cursor = mCursorAdapter.getCursor();
      if (cursor.moveToPosition(position)) {
        String submissionId = cursor.getString(SubmissionListFragment.COL_SUBMISSION_ID);
        // Check if an item was deleted, but the user clicked it before the UI removed it
        Intent intent = new Intent(mContext, SubmissionDetailActivity.class);
        Uri submissionUri =
            RedditContract.SubmissionsEntry.buildSubmissionUri(Integer.parseInt(submissionId));
        intent.setData(submissionUri);
        mContext.startActivity(intent);
      }
    }
  }
}
