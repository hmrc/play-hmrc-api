import uk.gov.hmrc.playcrosscompilation.AbstractPlayCrossCompilation
import uk.gov.hmrc.playcrosscompilation.PlayVersion._

object PlayCrossCompilation extends AbstractPlayCrossCompilation(defaultPlayVersion = Play26) {

  override lazy val playDir: String = playVersion match {
    case Play26 => "play-26"
    case Play27 => "play-27"
    case Play28 => "play-27"
  }

}
