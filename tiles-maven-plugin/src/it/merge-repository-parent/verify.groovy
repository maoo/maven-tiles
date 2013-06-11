def effectivePomFile = new File(basedir, "merge-tile/target/effective-pom.xml");
def effectivePom = effectivePomFile.getText();

assert effectivePom.contains("<url>http://download.java.net/maven/2</url>");

assert effectivePom.contains("<url>https://repository.jboss.org/nexus</url>");

return true;