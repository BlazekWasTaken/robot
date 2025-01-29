@file:Suppress("SameParameterValue")

package org.example

import processing.core.PApplet
import processing.core.PShape

class Processing : PApplet() {
    private val screenHeight = 1000
    private val screenWidth = 1000

    private val speed = 0.06f

    private object Position {
        var move = 0f
        var rotate = 0f
    }

    private lateinit var robot: PShape

    override fun settings() {
        size(screenWidth, screenHeight, P3D)
    }

    override fun setup() {
        background(0)
        robot = loadShape("src/main/resources/robot.obj")
        robot.disableStyle()
    }

    override fun draw() {
        background(100)
        smooth()

        ambientLight(40f, 20f, 20f)
        directionalLight(0f, 255f, 0f, 1f, 1f, -1f)
        directionalLight(255f, 0f, 100f, -1f, -1f, -1f)

        val r = 1f * max(screenWidth, screenHeight)
        val theta = PI / 2f
        val phi = map(mouseX.toFloat(), 0f, screenWidth.toFloat(), -PI, PI)
        println("$mouseX $mouseY")

        val x = r * sin(phi) * cos(theta)
        val y = r * sin(phi) * sin(theta)
        val z = r * cos(phi)
        camera(x, y, z, screenHeight / 2f, screenWidth / 2f, 0f, 0f, 1f, 0f)

        noStroke()
        noFill()
        fill(255)

        if (keyPressed) {
            when (key) {
                'w' -> Position.move += 10
                's' -> Position.move -= 10
                'a' -> Position.rotate += speed
                'd' -> Position.rotate -= speed
            }
        }

        translate(screenWidth / 2f, screenHeight / 2f, Position.move)
        rotateY(Position.rotate)

        showRobot()
    }

    private fun showRobot() {
        matrix {
            val boxHeight = this.screenHeight / 3.5f
            val boxWidth = screenWidth / 5f
            val boxDepth = screenWidth / 20f
            box(boxWidth, boxHeight, boxDepth)

            showHead(boxHeight)

            showArm(boxWidth, boxHeight, boxDepth, true)
            showArm(boxWidth, boxHeight, boxDepth, false)

            showLeg(boxWidth, boxHeight, boxDepth, true)
            showLeg(boxWidth, boxHeight, boxDepth, false)
        }
    }

    private fun showHead(boxHeight: Float) {
        matrix {
            translate(0f, -1 * boxHeight / 1.4f, 0f)
            scale(60f)
            rotateZ(PI)
            rotateY(PI * 3 / 2 + PI / 4 * sin(frameCount * speed * 1.5f))
            shape(robot)
        }
    }

    private fun showArm(boxWidth: Float, boxHeight: Float, boxDepth: Float, position: Boolean) {
        val positionInt = if (position) 1 else -1
        matrix {
            val rotate = positionInt * PI / 8 * sin(frameCount * speed)
            translate(positionInt * boxWidth / 2, -1 * boxHeight / 2.1f, 0f)
            rotateX(rotate)
            translate(0f, boxHeight / 3f, 0f)

            box(boxWidth / 4f, boxHeight / 2f, boxDepth / 1.5f)

            matrix {
                translate(0f, boxHeight / 4f, boxHeight / 7f)
                rotateX(PI / 5 * -rotate - PI / 1.5f)

                box(boxWidth / 4f, boxHeight / 3f, boxDepth / 3f)

                matrix {
                    translate(0f, -boxHeight / 6f, 0f)
                    box(boxWidth / 3f, boxHeight / 5f, boxDepth / 1.5f)
                    matrix {
                        translate(-boxWidth / 8f, -boxHeight / 9f, -boxDepth / 5f)
                        box(15f, 50f, 15f)
                    }
                    matrix {
                        translate(0f, -boxHeight / 9f, -boxDepth / 5f)
                        box(15f, 50f, 15f)
                    }
                    matrix {
                        translate(boxWidth / 8f, -boxHeight / 9f, -boxDepth / 5f)
                        box(15f, 50f, 15f)
                    }
                }
            }
        }
    }

    private fun showLeg(boxWidth: Float, boxHeight: Float, boxDepth: Float, position: Boolean) {
        val positionInt = if (position) 1 else -1
        matrix {
            val rotate = if (position) (sin(frameCount * speed) + 1) else (-sin(frameCount * speed) + 1)
            translate(positionInt * boxWidth / 2.5f, boxHeight / 2.1f, 0f)
            rotateX(PI / 6 * rotate)
            translate(0f, boxHeight / 4f, 0f)

            box(boxWidth / 4f, boxHeight / 2f, boxDepth)

            matrix {
                translate(0f, boxHeight / 2f + -30 * rotate, 0f + -25 * rotate)
                rotateX(PI / 6 * -rotate)

                box(boxWidth / 4f, boxHeight / 2f, boxDepth)
            }
        }
    }

    private fun matrix(function: () -> Unit) {
        pushMatrix()
        function()
        popMatrix()
    }
}