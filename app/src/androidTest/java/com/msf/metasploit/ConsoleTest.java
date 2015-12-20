
package com.msf.metasploit;

import com.msf.metasploit.rpc.MsfRpc;
import com.msf.metasploit.rpc.RpcConstants;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;

public class ConsoleTest extends TestCase {

	private MsfRpc msfRpc;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        if (msfRpc == null) {
            msfRpc = TestConstants.connect();
        }
    }

	public void testKillConsoles() throws Exception {
		Object consoles = msfRpc.execute(RpcConstants.CONSOLE_LIST);
		HashMap<String, List> consoleMap = (HashMap<String, List>) consoles;
        System.err.println("map " + consoleMap);
		List consoleList = consoleMap.get("consoles");
		System.err.println("list " + consoleList);
//		System.err.println("list " + consoleList.getClass().getSimpleName());

		for (Object item : consoleList) {
			HashMap<String, String> consoleResult = (HashMap<String, String>)item;
//			System.err.println("id " + consoleResult.get("id"));
			String id = consoleResult.get("id");
//			System.err.println("ids " + id);
			Object result = msfRpc.execute(RpcConstants.CONSOLE_DESTROY, new Object[]{id});
//			System.err.println("result " + result);
//			System.err.println("result " + result.getClass().getSimpleName());
		}

        consoles = msfRpc.execute(RpcConstants.CONSOLE_LIST);
        consoleMap = (HashMap<String, List>) consoles;
        consoleList = consoleMap.get("consoles");
        assertEquals(0, consoleList.size());
	}

    public void testCreateConsole() throws Exception {
		Object consoleId = msfRpc.execute(RpcConstants.CONSOLE_CREATE);
		HashMap<String, String> consoleInfo = (HashMap<String, String>)consoleId;
		assertEquals("msf > ", consoleInfo.get("prompt"));

		testKillConsoles();
	}

    public void testWriteConsole() throws Exception {
		Object consoleId = msfRpc.execute(RpcConstants.CONSOLE_CREATE);
		HashMap<String, String> consoleInfo = (HashMap<String, String>)consoleId;
		assertEquals("msf > ", consoleInfo.get("prompt"));
		String id = consoleInfo.get("id");
        HashMap<String, String> consoleMap;
		consoleMap = (HashMap<String, String>) msfRpc.execute(RpcConstants.CONSOLE_READ, new Object[] { id });
        System.err.println("c" + consoleMap.get("data"));

		Object result = msfRpc.execute(RpcConstants.CONSOLE_WRITE, new Object[] { id, "use exploit/multi/handler\n" });
		System.err.println("result " + result);
		System.err.println("result " + result.getClass().getSimpleName());

		consoleMap = (HashMap<String, String>) msfRpc.execute(RpcConstants.CONSOLE_READ, new Object[] { id });
		result = consoleMap;
		System.err.println("result " + result);
		System.err.println("result " + result.getClass().getSimpleName());
		System.err.println("c" + consoleMap.get("data"));
		String prompt = consoleMap.get("prompt");
		prompt = prompt.replaceAll("\\x01|\\x02", "");
//		String hexPrompt = Utils.bytesToHex(prompt.getBytes());
//		System.err.println("hex " + hexPrompt);
		assertEquals("msf exploit(handler) > ", prompt);

		testKillConsoles();
	}

}
