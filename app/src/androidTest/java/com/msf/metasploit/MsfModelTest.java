
package com.msf.metasploit;

import android.test.InstrumentationTestCase;

import com.msf.metasploit.rpc.MsfController;

public class MsfModelTest extends InstrumentationTestCase {

    private MsfController msfController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        if (msfController == null) {
//            msfController = MsfController.getInstance();
            TestConstants.connect();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPlugins() throws Exception {

//        Intent intent = msfController.runCmd(RpcConstants.PLUGIN_LOADED);
//        MsfModel.getInstance().updateModel(intent);
//        assertEquals(2, intent);
//
//        Map<String, String> options = new HashMap<String, String>();
//        options.put(GCMController.PROPERTY_API_KEY, "");
//        options.put(GCMController.PROPERTY_REG_ID,
//                GCMController.getRegistrationId(MsfApplication.getApplication()));
//        Object o = msfController.runCmd(RpcConstants.PLUGIN_LOAD, new
//                Object[] {
//                        "gcm_notify", options
//                });
//        assertEquals(2, o);
    }

    public void testJobs() throws Exception {

        /*
        Intent intent = msfController.runCmd(RpcConstants.JOB_LIST);
        MsfModel.getInstance().updateModel(intent);

        List<Job> jobs = MsfModel.getInstance().getJobs();
        assertEquals(0, jobs.size());

        Map options = new HashMap();
        options.put("PAYLOAD", "generic/shell_reverse_tcp");
        // options.put("PAYLOAD", "windows/meterpreter/reverse_https");
        options.put("LHOST", "192.168.0.3");
        options.put("LPORT", "4003");

        Object[] args = new Object[] {
                "exploit", "multi/handler", options
        };

        msfController.runCmd(RpcConstants.MODULE_EXECUTE, args);

        Object[] args2 = new Object[] {
                jobs.get(0).id
        };
        intent = msfController.runCmd(RpcConstants.JOB_INFO, args2);
        MsfModel.getInstance().updateModel(intent);

        jobs = MsfModel.getInstance().getJobs();
        assertEquals(1, jobs.size());
        */

        // Map options = new HashMap();
        // options.put("LHOST", "192.168.0.3");
        // options.put("LPORT", "4002");
        // Object[] args = new Object[] {
        // "exploit", "multi/handler", options
        // };
        // HashMap<String, Object> exploit = (HashMap<String, Object>)
        // msfController.runCmd(RpcConstants.MODULE_EXECUTE, args);

        // assertEquals(0, exploit);
        // Integer id = (Integer) exploit.get("job_id");
        // assertNotNull(id);
        //
        // // HashMap<String, Object> jobs2 = (HashMap<String, Object>)
        // msfController.runCmd(RpcConstants.JOB_LIST);
        // assertEquals(1, jobs2.size());
    }

    public void testSessions() throws Exception {
        // testJobs();
        // List<Session> sessions =
        // Session.getList(msfController.runCmd(RpcConstants.SESSION_LIST));
        // assertEquals(2, sessions.size());

        // Object stopid = new Object[] { "2" };
        // HashMap<String, Object> modules = (HashMap<String, Object>)
        // msfController.runCmd(RpcConstants.SESSION_COMPATIBLE_MODULES,
        // stopid);
        // assertEquals(1, modules);
    }

    public void testModuleExecute() {

        // Map options = new HashMap();
        // options.put(GCMController.PROPERTY_API_KEY, GCMController.API_KEY);
        // options.put(GCMController.PROPERTY_REG_ID,
        // GCMController.getRegistrationId(MsfApplication.getApplication()));
        // Object o = msfController.runCmd(RpcConstants.PLUGIN_LOAD, new
        // Object[] {
        // "gcm_notify", options });
        // assertEquals(2, o);

        // Map options = new HashMap();
        // options.put("PAYLOAD", "post/multi/gather/env");
        // options.put("LHOST", "0.0.0.0");
        // options.put("LPORT", "8888");
        // Object[] args = new Object[] {
        // "exploit", "multi/handler", options
        // };
        //
        // HashMap<String, Object> exploit = (HashMap<String, Object>)
        // msfController.runCmd(RpcConstants.MODULE_EXECUTE, args);
        // Integer id = (Integer) exploit.get("job_id");
        // assertNotNull(id);
    }

    public void testConsoleModel() {
        


    }

}
