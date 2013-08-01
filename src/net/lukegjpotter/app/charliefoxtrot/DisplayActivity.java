package net.lukegjpotter.app.charliefoxtrot;

import java.util.Arrays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class DisplayActivity extends Activity {

	private SharedPreferences queryHistory; // The User's favourite queries.
	private TableLayout queryTableLayout;   // Shows the Search Buttons.
	private EditText queryEditText;         // Where the user enters queries.
	private ImageButton queryButton;        // Button that triggers the querying process.
	private Button clearQueryHistoryButton; // Button that clears the users saved queries.
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		
		// Get the Shared Preferences that contain the user's saved Queries.
		queryHistory = getSharedPreferences("saved", MODE_PRIVATE);
		
		initialiseViews();
		refreshButtons(null);
	}

	private void initialiseViews() {
		
		queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);
		queryEditText = (EditText) findViewById(R.id.queryEditText);
		
		queryButton = (ImageButton) findViewById(R.id.queryButton);
		queryButton.setOnClickListener(saveQueryButtonListener);
		
		clearQueryHistoryButton = (Button) findViewById(R.id.clearSavedQueries);
		clearQueryHistoryButton.setOnClickListener(clearQueryHistoryButtonListener);
	}

	/**
	 * Recreate search queries and edit Buttons for all saved queries.
	 * Pass Null to create all the tag and edit buttons.
	 * 
	 * @param newTag
	 */
	private void refreshButtons(String newTag) {
		
		// Store saved queries in the queries array.
		String[] tags = queryHistory.getAll().keySet().toArray(new String[0]);
		Arrays.sort(tags, String.CASE_INSENSITIVE_ORDER);
		
		// If a new query is saved, insert in GUI at the appropriate location.
		if (newTag != null) {
			
			makeTagGui(newTag, Arrays.binarySearch(tags, newTag));
		} else {
			
			for (int i = 0; i < tags.length; ++i) {
				
				makeTagGui(tags[i], i);
			}
		}
	}

	/**
	 * Add a new query to the save file, then refresh all buttons.
	 * 
	 * @param query
	 * @param tag
	 */
	private void makeTag(String query, String tag) {
		
		// originalQuery will be null if we're modifying an existing search.
		String originalQuery = queryHistory.getString(tag, null);
		
		// Check if the button already exists. 
		if (queryHistory.contains(originalQuery) == false) {
			
			// Get a SharedPreferences.Editor to store new tag/query pair.
			SharedPreferences.Editor prefsEditor = queryHistory.edit();
			prefsEditor.putString(tag, query);
			prefsEditor.apply();
		}
		
		if (originalQuery == null) {
			
			refreshButtons(tag);
		}
	}
	
	/**
	 * Takes in the string and pops up a new AlertDialog with the phonetic
	 * representation of the query.
	 * 
	 * @param query
	 */
	private void displayPhonetics(String query) {
		
		// Create a new DeterminePhoneticRepresentation object.
		DeterminePhoneticRepresentation dpr = new DeterminePhoneticRepresentation();
		
		// Pass it the contents from the queryEditText.
		String phoneticRepresentation = dpr.parseString(query);
		
		// Clear the Edit Text.
		queryEditText.setText("");
		
		// Create a new AlertDialog to display the words.
		AlertDialog.Builder builder = new AlertDialog.Builder(DisplayActivity.this);
		
		builder.setTitle(R.string.parsedTitle);
		builder.setMessage(phoneticRepresentation);
		builder.setPositiveButton(R.string.OK, null);
		
		// Create the AlertDialog from the AlertDialog Builder.
		AlertDialog parsedDialog = builder.create();
		parsedDialog.show();
		
		// Save the query to the history.
		if (query.length() >= 10) {
			
			makeTag(query, query.substring(0, 10));
		} else {
			
			makeTag(query, query);
		}
	}
	
	/**
	 * Add a new Tag Button and corresponding EditText Button to the GUI.
	 * 
	 * @param tag
	 * @param index
	 */
	private void makeTagGui(String tag, int index) {
		
		// Get a reference to the LayoutInflator service.
		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Inflate the view_new_saved_query.xml to create a new tag and edit Buttons.
		View newSaveQueryView = inflator.inflate(R.layout.view_new_saved_query, null);
		
		// Get newTagButton, set its text and register its listener.
		Button newTagButton = (Button) newSaveQueryView.findViewById(R.id.newTagButton);
		newTagButton.setText(tag);
		newTagButton.setOnClickListener(historicQueryButtonListener);
		
		// Get newClearButton and register its listener.
		Button newClearButton = (Button) newSaveQueryView.findViewById(R.id.newClearButton);
		newClearButton.setOnClickListener(clearButtonListener);
		
		// Add the new tag and clear buttons to the queryTableLayout.
		queryTableLayout.addView(newSaveQueryView, index);
	}
	
	/**
	 * Remove all saved query Buttons from the app.
	 */
	private void clearButtons() {
		
		// Remove all saved query Buttons.
		queryTableLayout.removeAllViews();
	}
	
	/**
	 * Create a new Button and add it to the ScrollView
	 */
	public OnClickListener saveQueryButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			// If the queryEditText is not empty
			if (queryEditText.getText().length() > 0) {
				
				displayPhonetics(queryEditText.getText().toString());
			} else {
				
				// Display an error message.
				AlertDialog.Builder builder = new AlertDialog.Builder(DisplayActivity.this);
				
				builder.setTitle(R.string.missingTitle);
				builder.setMessage(R.string.missingMessage);
				builder.setPositiveButton(R.string.OK, null);
				
				// Create the AlertDialog from the AlertDialog Builder.
				AlertDialog errorDialog = builder.create();
				errorDialog.show();
			}
		}
	};
	
	/**
	 * Clears the history of queries.
	 */
	public OnClickListener clearQueryHistoryButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			// Create a new AlertDialog Builder.
			AlertDialog.Builder builder = new AlertDialog.Builder(DisplayActivity.this);
			
			builder.setTitle(R.string.confirmDeleteTitle);
			builder.setMessage(R.string.confirmDeleteMessage);
			builder.setCancelable(true);
			builder.setNegativeButton(R.string.cancel, null);
			builder.setPositiveButton(R.string.confirmDelete, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					clearButtons();
					
					SharedPreferences.Editor prefsEditor = queryHistory.edit();
					prefsEditor.clear();
					prefsEditor.apply();
				}
			});
			
			// Create the AlertDialog from the AlertDialog Builder.
			AlertDialog confirmDeleteDialog = builder.create();
			confirmDeleteDialog.show();
		}
	};
	
	/**
	 * A listener for a button in the history section.
	 */
	public OnClickListener historicQueryButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String buttonText = ((Button) v).getText().toString();
			String query = queryHistory.getString(buttonText, null);
			
			displayPhonetics(query);
		}
	};
	
	/**
	 * A listener for the clear single history item.
	 */
	public OnClickListener clearButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) { // This View is the clear button.
			
			TableRow buttonTR = (TableRow) v.getParent();
			Button textButton = (Button) buttonTR.findViewById(R.id.newTagButton);
			String buttonText = textButton.getText().toString();
			
			SharedPreferences.Editor prefsEditor = queryHistory.edit();
			prefsEditor.remove(buttonText);
			prefsEditor.commit();
			//prefsEditor.apply();
			
			//queryTableLayout.removeView(v.get);
			clearButtons();
			refreshButtons(null);
		}
	};
}
