import microsites._

enablePlugins(MicrositesPlugin)
scalaVersion := "2.11.8"

micrositeExtraMdFiles := Map(
  file("README.md") -> ExtraMdFileConfig(
    "index.md",
    "home"
  )
)
micrositeImgDirectory := (resourceDirectory in Compile).value / "images"
micrositeCssDirectory := (resourceDirectory in Compile).value / "styles"
