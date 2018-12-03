//package br.uece.lotus.msc.app.runtime;
//
//import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
//import br.uece.lotus.msc.app.project.ProjectExplorerPluginMSC;
//import br.uece.seed.ext.ExtensionManager;
//import br.uece.seed.ext.Plugin;
//
//public class RuntimePlugin extends Plugin {
//
//    private ProjectExplorerPluginMSC projectExplorerDS;
//    private HmscComponent hmscComponent;
//
//    @Override
//    public void onStart(ExtensionManager extensionManager) throws Exception {
//        projectExplorerDS = extensionManager.get(ProjectExplorerPluginMSC.class);
//         hmscComponent = projectExplorerDS.getSelectedComponentBuildDS();
//
//    }
//
//    @Override
//    public void onStop(ExtensionManager extensionManager) throws Exception {
//        super.onStop(extensionManager);
//    }
//
//    public HmscComponent getParallelComponent() {
//        return hmscComponent;
//    }
//}
