import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Tests {

    private val input = """p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>
        |p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>""".trimMargin()

    private val puzzle = Tests::class.java.getResource("input").readText()

    @Test
    fun `it should parse one particle`() {
        val particle = Particle.parse("p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>")

        assertThat(particle.position).isEqualTo(Coordinate(3, 0, 0))
        assertThat(particle.velocity).isEqualTo(Coordinate(2, 0, 0))
        assertThat(particle.acceleration).isEqualTo(Coordinate(-1, 0, 0))
    }

    @Test
    fun `it should parse multiple particles`() {
        val particles = parse(input)

        assertThat(particles).hasSize(2)
    }

    @Test
    fun `it should tick particle`() {
        val particle = Particle.parse("p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>")

        val newParticle = particle.tick()

        assertThat(newParticle.position).isEqualTo(Coordinate(4, 0, 0))
        assertThat(newParticle.velocity).isEqualTo(Coordinate(1, 0, 0))
        assertThat(newParticle.acceleration).isEqualTo(Coordinate(-1, 0, 0))
    }

    @Test
    fun `it should tick several times a particle`() {
        val particle = Particle.parse("p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>")

        val newParticle = tick(particle, 3)

        assertThat(newParticle.position).isEqualTo(Coordinate(3, 0, 0))
        assertThat(newParticle.velocity).isEqualTo(Coordinate(-1, 0, 0))
        assertThat(newParticle.acceleration).isEqualTo(Coordinate(-1, 0, 0))
    }

    @Test
    fun `it should compute distance from origin`() {
        val particle = Particle.parse("p=<-13053,-6894,1942>, v=<14,39,-11>, a=<16,7,-2>")

        assertThat(particle.distance()).isEqualTo(21889)
    }

    @Test
    fun `it should find closest particle in the long term`() {
        val closest = closest(input)

        assertThat(closest).isEqualTo(0)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val closest = closest(puzzle, 10_000)

        assertThat(closest).isEqualTo(157)
    }

    @Test
    fun `it should remove particles colliding`() {
        val particles = collide("""p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>
            |p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>
            |p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>
            |p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>""".trimMargin())

        assertThat(particles).hasSize(1)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val particles = collide(puzzle, 10_000)

        assertThat(particles).hasSize(499)
    }
}
