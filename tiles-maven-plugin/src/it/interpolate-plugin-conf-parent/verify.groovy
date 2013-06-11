def effectivePomFile = new File(basedir, "merge-tile/target/effective-pom.xml");
def effectivePom = effectivePomFile.getText();

assert effectivePom.contains("<version>2.15</version>");

assert effectivePom.contains("<trimStackTrace>false</trimStackTrace>");

return true;