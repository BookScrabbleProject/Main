package ViewModel.tests;
import Model.HostModel;
import Model.GuestModel;
import Model.gameClasses.BookScrabbleHandler;
import Model.gameClasses.MyServer;
import ViewModel.ViewModel;

public class ViewModelTest {

    public static void main(String[] args) {
        MyServer server = new MyServer(1234, new BookScrabbleHandler());
        HostModel host = HostModel.getHost();
        host.connectToBookScrabbleServer(1235, "localhost", 1234);
        GuestModel guest = new GuestModel("localhost", 1235, "guest");

        ViewModel viewModel = new ViewModel(guest);
        guest.connectToHostServer();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        GuestModel guest2 = new GuestModel("localhost", 1235, "guest2");
        guest2.connectToHostServer();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        server.stop();
        host.getHostServer().close();
        guest.getClientCommunication().close();
        guest2.getClientCommunication().close();

        System.out.println("done!");
    }
}
