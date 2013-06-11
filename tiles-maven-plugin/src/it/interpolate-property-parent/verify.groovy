def file = new File(basedir, "merge-tile/target/classes/mytext.txt");
assert file.exists();

def effectivePomFile = new File(basedir, "merge-tile/target/effective-pom.xml");
def effectivePom = effectivePomFile.getText();
assert effectivePom.contains("<another.tile.property>a-tile-value</another.tile.property>");