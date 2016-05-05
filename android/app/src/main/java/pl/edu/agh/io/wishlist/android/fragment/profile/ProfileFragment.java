package pl.edu.agh.io.wishlist.android.fragment.profile;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.io.wishlist.android.ItemDetailActivity;
import pl.edu.agh.io.wishlist.android.R;
import pl.edu.agh.io.wishlist.android.dagger.DaggerApplication;
import pl.edu.agh.io.wishlist.android.domain.User;
import pl.edu.agh.io.wishlist.android.rest.LoadResourceTask;
import pl.edu.agh.io.wishlist.android.rest.ServerCredentials;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class ProfileFragment extends Fragment {

    @Bind(R.id.user_avatar)
    ImageView userAvatar;

    @Bind(R.id.username)
    TextView usernameTextView;

    @Bind(R.id.profile_stat1)
    TextView stat1;

    @Bind(R.id.profile_stat2)
    TextView stat2;

    @Bind(R.id.profile_stat3)
    TextView stat3;

    @Bind(R.id.giftList)
    ListView giftListView;

    @Inject
    ServerCredentials credentials;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    RestTemplate restTemplate;

    @Inject
    GiftArrayAdapter adapter;

    private LayoutInflater inflater;

    private EditText snackSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Dagger injection
        DaggerApplication.inject(this);

        // ButterKnife injection
        ButterKnife.bind(this, view);

        // FAB listener
        getActivity().findViewById(R.id.fab).setOnClickListener(new FabListener());

        String username = sharedPreferences.getString("username", "username");
        usernameTextView.setText(username);

        // adapter
        giftListView.setAdapter(adapter);

        // load resource
        new LoadResourceTask<User>(restTemplate, credentials.getUrl("users/" + username), User.class) {
            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    updateViews(user);
                } else {
                    Toast.makeText(getActivity(), "Can't load user information from server", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // ButterKnife unbind
        ButterKnife.unbind(this);
    }

    private void updateViews(User user) {
        stat1.setText(String.valueOf(user.getFriends().size()));
        stat2.setText(String.valueOf(user.getGifts().size()));
        // TODO add something to 3rd stat
//                stat3.setText(user.getUsername().charAt(0));

        adapter.clear();
        adapter.addAll(user.getGifts());
        adapter.notifyDataSetChanged();
    }

    @OnItemClick(R.id.giftList)
    void onItemClick(int position) {
        Toast.makeText(getActivity(), "Clicked position " + position + "!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.GIFT_EXTRA, adapter.getItem(position));
        startActivity(intent);
    }

    class FabListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // Create the Snackbar
            Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
            // Get the Snackbar's layout view
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            // Hide the text
            TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
            textView.setVisibility(View.INVISIBLE);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

            // Inflate our custom view
            View snackView = inflater.inflate(R.layout.snackbar_search, null);

            snackSearch = (EditText) snackView.findViewById(R.id.snackbar_search);
            snackSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);

            // Add the view to the Snackbar's layout
            layout.addView(snackView, 0);

            // Set action
            snackbar.setAction("SEARCH", new FabSearchListener());

            // Show the Snackbar
            snackbar.show();
        }

        class FabSearchListener implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Toast.makeText(getActivity(), "ACTION" + snackSearch.getText(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}