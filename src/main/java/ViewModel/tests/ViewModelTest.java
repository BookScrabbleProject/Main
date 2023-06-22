package ViewModel.tests;

import Model.HostModel;
import Model.GuestModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;
import ViewModel.ViewModel;
import ViewModel.DataChanges;


public class ViewModelTest {

    public static void main(String[] args) {
        try {
            MyServer server = new MyServer(1234, new BookScrabbleHandler());
            server.start();
            HostModel host = HostModel.getHost();
            host.connectToBookScrabbleServer(1235, "localhost", 1234);
            GuestModel guest = new GuestModel("localhost", 1235, "guest");

            ViewModel viewModel = ViewModel.getViewModel();
            viewModel.setModel(host);
            System.out.println(">>> connecting to host (guest1-VM) <<<");
            guest.connectToHostServer();

            Thread.sleep(2000);

            GuestModel guest2 = new GuestModel("localhost", 1235, "testGuest2");
            System.out.println("\n>>> connecting to host (guest2) <<<");
            guest2.connectToHostServer();

            Thread.sleep(1000);

            GuestModel guest3 = new GuestModel("localhost", 1235, "testGuest3");
            System.out.println("\n>>> connecting to host (guest3) <<<");
            guest3.connectToHostServer();
            Thread.sleep(1000);

            GuestModel guest4 = new GuestModel("localhost", 1235, "testGuest3");
            System.out.println("\n>>> connecting to host (guest4) <<<");
            guest4.connectToHostServer();

            Thread.sleep(1000);

//            TODO: fix this - socket invokes close() method on server
            System.out.println("\n>>> starting game <<<");
            host.startGame();

            Thread.sleep(2000);
            host.getConnectedPlayers().get(2).setScore(5);
            System.out.println("\n>>> taking tile from bag <<<");
//            viewModel.takeTileFromBag();

            DataChanges d1 = new DataChanges('w', 7, 7);
            DataChanges d2 = new DataChanges('e', 7, 8);
            DataChanges d3 = new DataChanges('e', 7, 9);
            DataChanges d4 = new DataChanges('k', 7, 10);
            viewModel.changesList.add(d1);
            viewModel.changesList.add(d2);
            viewModel.changesList.add(d3);
            viewModel.changesList.add(d4);
            viewModel.tryPlaceWord();

            Thread.sleep(5000);
            System.out.println("\n>>> closing connections <<<");
//            viewModel.getModel().closeConnection();
            viewModel.close();
            guest2.closeConnection();
            guest3.closeConnection();
            guest4.closeConnection();
            host.closeConnection();
            server.stop();

            System.out.println("done!");
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }
}
