# Chips Input Layout
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

### Usage in an XML layout
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
