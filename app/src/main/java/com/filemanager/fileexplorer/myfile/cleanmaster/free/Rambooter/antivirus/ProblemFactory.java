package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.antivirus;


public class ProblemFactory implements IFactory<IProblem> {

    @Override
    public IProblem createInstance(String str) throws IllegalArgumentException {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 96801:
                if (str.equals("app")) {
                    c = 0;
                    break;
                }
                break;
            case 116100:
                if (str.equals(DebugUSBEnabledProblem.kSerializationType)) {
                    c = 1;
                    break;
                }
                break;
            case 1160608119:
                if (str.equals(UnknownAppEnabledProblem.kSerializationType)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new AppProblem();
            case 1:
                return new DebugUSBEnabledProblem();
            case 2:
                return new UnknownAppEnabledProblem();
            default:
                throw new IllegalArgumentException("Unknown node type creating IProblem");
        }
    }
}
