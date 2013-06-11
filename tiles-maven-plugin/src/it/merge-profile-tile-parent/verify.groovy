def effectivePomFile = new File(basedir, "merge-tile/target/effective-pom.xml");
def effectivePom = effectivePomFile.getText();

assert effectivePom.contains("<artifactId>maven-enforcer-plugin</artifactId>");

return true;