
package com.msf.metasploit;

import android.content.Intent;

import com.msf.metasploit.model.Session;
import com.msf.metasploit.rpc.MsfController;
import com.msf.metasploit.rpc.RpcConstants;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;

public class MsfControllerTest extends TestCase {

    private MsfController msfController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        if (msfController == null) {
            msfController = MsfController.getInstance();
            TestConstants.connect();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        killJobs();
    }

    Object runCmd(String cmd,Object[] args) {
        Intent intent = msfController.runCmd(cmd,args);
        return intent.getSerializableExtra(MsfController.RESULT);
    }
    Object runCmd(String cmd) {
        return runCmd(cmd, null);
    }

    public void killJobs() {
        HashMap<String, Object> jobs = (HashMap<String, Object>) runCmd(RpcConstants.JOB_LIST);

        for (Object object : jobs.keySet()) {
            // Object stopid = new Object[] {
            // object
            // };
            // HashMap<String, Object> jobstop = (HashMap<String, Object>)
            // msfController.runCmd(RpcConstants.JOB_STOP, stopid);
            // assertEquals("success", jobstop.get("result"));
        }

    }

    public void testVersion() throws Exception {

        HashMap<String, String> version = (HashMap<String, String>) runCmd(RpcConstants.CORE_VERSION);
        assertEquals("1.0", version.get("api"));

    }

    public void skiptestJobs() throws Exception {

//        Job.getList(msfController.runCmd(RpcConstants.JOB_LIST));

        // String job = "job";
        // Object object = RpcObject.getList(job);
        assertEquals(new HashMap<>(), runCmd(RpcConstants.JOB_LIST));
        // assertEquals("lol", modules);

        HashMap options = new HashMap();
        options.put("PAYLOAD", "generic/shell_reverse_tcp");
        // options.put("PAYLOAD", "windows/meterpreter/reverse_https");
        options.put("LHOST", "192.168.0.3");
        options.put("LPORT", "4003");
        Object[] args = new Object[] {
                "exploit", "multi/handler", options
        };
        HashMap<String, Object> exploit = (HashMap<String, Object>) runCmd(RpcConstants.MODULE_EXECUTE, args);

        // Map options = new HashMap();
        // options.put("LHOST", "192.168.0.3");
        // options.put("LPORT", "4002");
        // Object[] args = new Object[] {
        // "exploit", "multi/handler", options
        // };
        // HashMap<String, Object> exploit = (HashMap<String, Object>)
        // msfController.runCmd(RpcConstants.MODULE_EXECUTE, args);

//        assertEquals(0, exploit);
//        Integer id = (Integer) exploit.get("job_id");
//        assertNotNull(id);

        HashMap<String, Object> jobs2 = (HashMap<String, Object>) runCmd(RpcConstants.JOB_LIST);
        assertEquals(1, jobs2.size());
    }

    public void testSessions() throws Exception {
        // testJobs();
        List<Session> sessions = Session.getList(runCmd(RpcConstants.SESSION_LIST));
        assertEquals(0, sessions.size());

        // Object stopid = new Object[] { "2" };
        // HashMap<String, Object> modules = (HashMap<String, Object>)
        // msfController.runCmd(RpcConstants.SESSION_COMPATIBLE_MODULES,
        // stopid);
        // assertEquals(1, modules);
    }
    
//    public void testModuleExecute() {
//         Map options = new HashMap();
//         options.put("PAYLOAD", "post/multi/gather/env");
//         options.put("LHOST", "0.0.0.0");
//         options.put("LPORT", "8888");
//         Object[] args = new Object[] {
//         "exploit", "multi/handler", options
//         };
//        
//         HashMap<String, Object> exploit = (HashMap<String, Object>)
//         msfController.runCmd(RpcConstants.MODULE_EXECUTE, args);
//         Integer id = (Integer) exploit.get("job_id");
//         assertNotNull(id);
//    }

}
