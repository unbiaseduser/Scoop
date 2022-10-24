package taco.scoop.core;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import taco.scoop.core.data.crash.Crash;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Crash>> crashes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>("");
    private Crash combinedCrash;

    public LiveData<List<Crash>> getCrashes() {
        return crashes;
    }

    public void setCrashes(List<Crash> crashes) {
        this.crashes.setValue(crashes);
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.setValue(searchTerm);
    }

    public LiveData<String> getSearchTerm() {
        return searchTerm;
    }

    public void setCombinedCrash(Crash combinedCrash) {
        this.combinedCrash = combinedCrash;
    }

    public Crash getCombinedCrash() {
        return combinedCrash;
    }
}
