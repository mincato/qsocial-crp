package com.qsocialnow.model;

import com.qsocialnow.common.model.config.Media;

public class MediaView {

    private Media media;

    private boolean checked;

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
