// USER editable file


package sifive.blocks.PWM

import chisel3._
// import chisel3.{withClockAndReset, _}
import chisel3.util._
import chisel3.experimental._

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.amba.axi4._
import freechips.rocketchip.amba.apb._
import freechips.rocketchip.amba.ahb._
import freechips.rocketchip.interrupts._
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.tilelink._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.regmapper._

import sifive.skeleton._
import sifive.blocks.util.{NonBlockingEnqueue, NonBlockingDequeue}



class LPWM(c: PWMParams)(implicit p: Parameters) extends LPWMBase(c)(p)
{

// User code here

}

class NPWMTop(c: NPWMTopParams)(implicit p: Parameters) extends NPWMTopBase(c)(p)
{

// User code here
  // route the ports of the black box to this sink
  val ioBridgeSink = BundleBridgeSink[PWMBlackBoxIO]()
  ioBridgeSink := imp.ioBridgeSource

  // logic to connect ioBridgeSink/Source nodes
  override lazy val module = new LazyModuleImp(this) {

    // connect the clock and negedge reset to the default clock and reset
    ioBridgeSink.bundle.PCLK    := clock.asUInt
    ioBridgeSink.bundle.PRESETn := !(reset.toBool)
  }

}

object NPWMTop {
  def attach(c: NPWMTopParams)(bap: BlockAttachParams): NPWMTop = {
    val PWM = NPWMTopBase.attach(c)(bap)

    // User code here
    implicit val p: Parameters = bap.p

    PWM
  }
}

class WithPWMTop extends Config(
  new WithPWMTopBase(
    //ctrl_base = 0x60000L
    ctrl_base = 0x40000L
  )

    // User code here
)
