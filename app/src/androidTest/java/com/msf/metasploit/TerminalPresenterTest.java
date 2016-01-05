
package com.msf.metasploit;

import android.test.AndroidTestCase;

import com.msf.metasploit.adapter.TerminalPresenter;
import com.msf.metasploit.model.DefaultRpcServer;
import com.msf.metasploit.model.RpcServer;
import com.msf.metasploit.model.Session;
import com.msf.metasploit.model.Terminal;

import java.io.IOException;
import java.util.List;

public class TerminalPresenterTest extends AndroidTestCase {

    private RpcServer rpcServer;

    public void testLogin() throws IOException {
        Msf msf = new Msf();
        RpcServer rpcServer = DefaultRpcServer.createDefaultRpcServer();
        msf.msfServerList.serverList.add(rpcServer);
        assertTrue(rpcServer.status == 0);
        msf.msfServerList.connectServer(rpcServer);
        assertTrue(rpcServer.status == RpcServer.STATUS_AUTHORISED);
        this.rpcServer = rpcServer;
    }

    public void testCreateConsole() throws IOException {
        if (rpcServer == null) {
            testLogin();
        }

        rpcServer.getRpc().updateModel();
        assertNotNull(rpcServer.getModel().getConsoles());
        assertNotNull(rpcServer.getModel().getJobs());
        assertNotNull(rpcServer.getModel().getSessions());
        TerminalPresenter terminalPresenter = new TerminalPresenter();
        terminalPresenter.setTerminal(rpcServer.getRpc(), null, Terminal.TYPE_CONSOLE);
        terminalPresenter.updateConsole();
        assertNotNull(terminalPresenter.getTerminal().id);
//        assertTrue(rpcServer.getModel().getSessions().size() > 0);
    }

    public void testSession() throws IOException {
        if (rpcServer == null) {
            testLogin();
        }
        rpcServer.getRpc().updateModel();
        TerminalPresenter terminalPresenter = new TerminalPresenter();
        List<Session> sessions = rpcServer.getModel().getSessions();
        assertNotNull(sessions);
        System.err.println("sessions " + sessions);
        Session session = sessions.get(0);
        terminalPresenter.setTerminal(rpcServer.getRpc(), session.id, session.type);
        terminalPresenter.updateConsole();
        terminalPresenter.sendCommand("id");
        terminalPresenter.updateConsole();
//        terminalPresenter.setSession(rpcServer.getRpc(), sessions.get(0));
//        terminalPresenter.updateConsole();
//        assertNotNull(terminalPresenter.getTerminal().id);
    }

}
