# Chips Input Layout
*Updates to documentation and project are still in progress*

A highly customizable Android ViewGroup for displaying Chips (specified in the Material Design Guide)!

<img src="https://github.com/tylersuehr7/chips-input-layout/blob/master/docs/screen_filterable_list.png" width="200"> <img src="https://github.com/tylersuehr7/chips-input-layout/blob/master/docs/screen_contact_chip_multiple.png" width="200"> <img src="https://github.com/tylersuehr7/chips-input-layout/blob/master/docs/screen_chips_multiple.png" width="200"> <img src="https://github.com/tylersuehr7/chips-input-layout/blob/master/docs/screen_contact_chip_details.png" width="200">

Here's some of the core features of this library:
* You can allow the user to filter chips as they type
* You can allow the user to enter custom chips
* You can specify if the chips show details, is deletable, or has an avatar
* You can specify how the chips are managed/stored in memory
* ChipsInputView can validate chips that are selected
* Most views (seen in screenshots) are highly customizable
* Sticks to the Material Design Guide as much as possible

*Honorable Mentions:*
* This was based on the design of: https://github.com/pchmn/MaterialChipsInput
* This uses a RecyclerView layout manager: https://github.com/BelooS/ChipsLayoutManager


## Usage as Chips Input
The basic usage of this library is to allow users to input chips and for them to be displayed like in the Material Design Guide. To achieve this functionality, you'll need to use the `ChipsInputLayout` view.

### Using in an XML layout
`ChipsInputLayout` can be used in any ViewGroup and supports all width and height attributes. Simple usage is shown here:
```xml
<com.tylersuehr.library.ChipsInputLayout
        android:id="@+id/chips_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Start typing for chips... "
        android:textColorHint="#757575"
        android:textColor="#212121"
        app:detailedChipsEnabled="true"
        app:customChipsEnabled="true"/>
```

Here is a table of all the XML attributes available for this view:

Attribute | Type | Summary
--- | --- | ---
`android:hint` | `string` | Hint shown in the chips input.
`android:textColorHint` | `color` | Text color of the hint shown in the chips input. 
`android:textColor` | `color` | Text color of chips input.
`app:detailedChipsEnabled` | `boolean` | True if clicking a chip should show its details.
`app:customChipsEnabled` | `boolean` | True if user is allowed to enter custom chips.
`app:maxRows` | `int` | Maximum number of rows used to display chips.
`app:chipTextColor` | `color` | Text color of each chips' title and subtitle.
`app:chipHasAvatarIcon` | `boolean` | True if each chip should show an avatar icon.
`app:chipDeletable` | `boolean` | True if each chip should be deletable by the user.
`app:chipDeleteIconColor` | `color` | Color of each chips' delete icon.
`app:chipBackgroundColor` | `color` | Color of each chips' background.
`app:detailedChipTextColor` | `color` | Text color of each detailed chips' title and subtitle.
`app:detailedChipBackgroundColor` | `color` | Color of each detailed chips' background.
`app:detailedChipDeleteIconColor` | `color` | Color of each detailed chips' delete icon.
`app:filterableListBackgroundColor` | `color` | Color of the filterable list's background.
`app:filterableListTextColor` | `color` | Text color of the filterable list's items.
`app:filterableListElevation` | `dimension` | Elevation of the filterable list.

### Using in Java code
`ChipsInputLayout` can be programmatically added into any ViewGroup. Simple usage in an Activity is shown here:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    ChipsInputLayout chipsInputLayout = new ChipsInputLayout(this);
    // Set any properties for chips input layout
    
    setContentView(chipsInputLayout);
}
```

Here is a table of all the accessible attributes available for this view:

Method | Summary
--- | ---
`setInputHint(CharSequence)` | Changes hint shown in the chips input.
`setInputHintTextColor(ColorStateList)` | Changes text color of the hint shown in the chips input. 
`setInputTextColor(ColorStateList)` | Changes text color of chips input.
`setShowDetailedChipsEnabled(boolean)` |  True if clicking a chip should show its details.
`setCustomChipsEnabled(boolean)` | True if user is allowed to enter custom chips.
`setMaxRows(int)` | Changes maximum number of rows used to display chips.
`setChipTitleTextColor(ColorStateList)` | Changes text color of each chips' title and subtitle.
`setShowChipAvatarEnabled(boolean)` | True if each chip should show an avatar icon.
`setChipsDeletable(boolean)` | True if each chip should be deletable by the user.
`setChipDeleteIconColor(ColorStateList)` | Changes color of each chips' delete icon.
`setChipBackgroundColor(ColorStateList)` | Changes color of each chips' background.
`setChipDeleteIcon(Drawable)` | Changes the each chips' delete icon.
`setChipDeleteIcon(int)` | Overload of setChipDeleteIcon(Drawable).
`setDetailedChipTextColor(ColorStateList)` | Changes text color of each detailed chips' title and subtitle.
`setDetailedChipBackgroundColor(ColorStateList)` | Changes color of each detailed chips' background.
`setDetailedChipDeleteIconColor(ColorStateList)` | Changes color of each detailed chips' delete icon.
`setFilterableListBackgroundColor(ColorStateList)` | Changes color of the filterable list's background.
`setFilterableListTextColor(ColorStateList)` | Changes text color of the filterable list's items.
`setFilterableListElevation(float)` | Changes elevation of the filterable list.

## Managing Chips
Where this library capitalizes, is how it decentralizes where and how the selected and filterable chips are stored. This makes accessing and receiving updates to data source changes from various Android components really simple. 

All chips are managed by, `ChipDataSource`, which is an abstraction to decouple the concrete implementation of how the abstract methods manage the chips. This means that other implementations of `ChipDataSource` can be made at your own leisure. *Currently, there is no method on `ChipsInputLayout` to programmatically change the data source; however, these will be future additions to the library.*

The current default implementation of `ChipDataSource` is `ListChipDataSource`, and it uses `ArrayList` as the list implementation for selected and filtered chip lists.

### Observing any change to the chip data source
`ChipDataSource` has the ability to notify observers that want to observe any type of change to the data in `ChipDataSource`. The observers will be notified if a chip has been added or removed from either the selected or filtered lists in the `ChipDataSource`; however, there's no information about the event though. 

This is used internally by the library to trigger UI updates on `RecyclerView` adapters when the data has changed.

*Currently, `ChipsInputLayout`, does not have a method to set this type of observer on the `ChipDataSource` because this is for internal components of the library. However, this may be an addition to the library, if needed\requested, in the future.*

To use this functionality, you'll want to implement the `ChipChangedObserver` and register it on `ChipDataSource`. Be sure to manage unregistering the observer, if need be, as well.

Here is a simple example:
```java
public class CoolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChipChangedObserver {
    public CoolAdapter(ChipDataSource dataSource) {
        dataSource.addChipChangedObserver(this);
    }
    
    // adapter implementation to do really cool adapter stuff...
    
    @Override
    public void onChipDataSourceChanged() {
        // This example would just update the Recycler when the chip data source changes
        notifyDataSetChanged();
    }
}
```
