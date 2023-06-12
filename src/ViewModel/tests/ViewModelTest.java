package ViewModel.tests;

import Model.HostModel;
import Model.GuestModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;
import ViewModel.ViewModel;

public class ViewModelTest {

    public static void main(String[] args) {
        try {


            MyServer server = new MyServer(1234, new BookScrabbleHandler());
            server.start();
            HostModel host = HostModel.getHost();
            host.connectToBookScrabbleServer(1235, "localhost", 1234);
            GuestModel guest = new GuestModel("localhost", 1235, "guest");

            ViewModel viewModel = new ViewModel(guest);
            guest.connectToHostServer();

            guest.tryPlaceWord("hello", 7, 7, true);

            Thread.sleep(2000);

            GuestModel guest2 = new GuestModel("localhost", 1235, "guest2");
            guest2.connectToHostServer();

            Thread.sleep(2000);

            guest.tryPlaceWord("hello", 7, 7, true);
            Thread.sleep(2000);

            server.stop();
            host.getHostServer().close();
            guest.getClientCommunication().close();
            guest2.getClientCommunication().close();

            System.out.println("done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
