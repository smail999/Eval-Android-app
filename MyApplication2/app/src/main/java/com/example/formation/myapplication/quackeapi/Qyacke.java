
package com.example.formation.myapplication.quackeapi;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Qyacke {

    @SerializedName("bbox")
    private List<Double> mBbox;
    @SerializedName("features")
    private List<Feature> mFeatures;
    @SerializedName("metadata")
    private Metadata mMetadata;
    @SerializedName("type")
    private String mType;

    public List<Double> getBbox() {
        return mBbox;
    }

    public List<Feature> getFeatures() {
        return mFeatures;
    }

    public Metadata getMetadata() {
        return mMetadata;
    }

    public String getType() {
        return mType;
    }

    public static class Builder {

        private List<Double> mBbox;
        private List<Feature> mFeatures;
        private Metadata mMetadata;
        private String mType;

        public Qyacke.Builder withBbox(List<Double> bbox) {
            mBbox = bbox;
            return this;
        }

        public Qyacke.Builder withFeatures(List<Feature> features) {
            mFeatures = features;
            return this;
        }

        public Qyacke.Builder withMetadata(Metadata metadata) {
            mMetadata = metadata;
            return this;
        }

        public Qyacke.Builder withType(String type) {
            mType = type;
            return this;
        }

        public Qyacke build() {
            Qyacke Qyacke = new Qyacke();
            Qyacke.mBbox = mBbox;
            Qyacke.mFeatures = mFeatures;
            Qyacke.mMetadata = mMetadata;
            Qyacke.mType = mType;
            return Qyacke;
        }

    }

}
