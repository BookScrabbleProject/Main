package ViewModel.tests;
import ViewModel.DataChanges;

import ViewModel.ViewModel;

public class ViewModelTest {

    public static void main(String[] args) {
        ViewModel viewModel = new ViewModel();
        viewModel.changesList.add(new DataChanges('a', 0, 0, 0, 0));
        viewModel.changesList.add(new DataChanges('b', 0, 1, 0, 0));
        viewModel.changesList.add(new DataChanges('c', 0, 2, 0, 0));
        viewModel.changesList.add(new DataChanges('d', 0, 3, 0, 0));
        if (viewModel.isChangeValid()) {
            System.out.println("Test 1 passed");
        } else {
            System.out.println("Test 1 failed");
        }

        System.out.println(viewModel.getWord());
        viewModel.changesList.clear();

        System.out.println("done!");
    }
}
