/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.cinaf.android_tv.ui.main;


import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

import com.cinaf.android_tv.data.models.Movie;


public class CardListRow extends ListRow {

    private Movie mCardRow;

    public CardListRow(HeaderItem header, ObjectAdapter adapter, Movie cardRow) {
        super(header, adapter);
        setmCardRow(cardRow);
    }


    public Movie getmCardRow() {
        return mCardRow;
    }

    public void setmCardRow(Movie mCardRow) {
        this.mCardRow = mCardRow;
    }
}
