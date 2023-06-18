package ViewModel.tests;

import Model.HostModel;
import Model.GuestModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;
import ViewModel.ViewModel;

import javax.swing.text.View;

public class ViewModelTest {

    public static void main(String[] args) {
        try {
            MyServer server = new MyServer(1234, new BookScrabbleHandler());
            server.start();
            HostModel host = HostModel.getHost();
            host.connectToBookScrabbleServer(1235, "localhost", 1234);
            GuestModel guest = new GuestModel("localhost", 1235, "guest");

            ViewModel viewModel = ViewModel.getViewModel();
            viewModel.setModel(guest);
            guest.connectToHostServer();

            Thread.sleep(2000);

            GuestModel guest2 = new GuestModel("localhost", 1235, "testGuest2");
            guest2.connectToHostServer();

            Thread.sleep(2000);

            host.startGame();

            Thread.sleep(2000);

            guest2.tryPlaceWord("week", 7, 7, true);
            Thread.sleep(5000);

            guest.getClientCommunication().close();
            guest2.getClientCommunication().close();
            host.getHostServer().close();
            server.stop();

            System.out.println("done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
