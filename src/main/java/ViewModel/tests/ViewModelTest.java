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

            ViewModel viewModel = ViewModel.getViewModel();
            viewModel.setModel(guest);
            System.out.println(">>> connecting to host (guest1-VM) <<<");
            guest.connectToHostServer();

            Thread.sleep(2000);

            GuestModel guest2 = new GuestModel("localhost", 1235, "testGuest2");
            System.out.println("\n>>> connecting to host (guest2) <<<");
            guest2.connectToHostServer();

//            TODO: fix this - socket invokes close() method on server
            Thread.sleep(2000);
            System.out.println("\n>>> starting game <<<");
            host.startGame();

            Thread.sleep(2000);
            System.out.println("\n>>> taking tile from bag <<<");
            viewModel.takeTileFromBag();

//            guest2.tryPlaceWord("week", 7, 7, true);
            Thread.sleep(5000);
            System.out.println("\n>>> closing connections <<<");
            viewModel.getModel().closeConnection();
            guest2.closeConnection();
            host.closeConnection();
            server.stop();

            System.out.println("done!");
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }
}
