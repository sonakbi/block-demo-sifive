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

class NPWMTopIO(
) extends Bundle {
  val capt0_event = Input(Bool())
  val capt1_event = Input(Bool())
  val capt2_event = Input(Bool())
  val capt3_event = Input(Bool())
  val capt4_event = Input(Bool())
  val capt5_event = Input(Bool())
}

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

  // create a new ports for capt0~5_event
  val ioBridgeSource = BundleBridgeSource(() => new NPWMTopIO())

  // logic to connect ioBridgeSink/Source nodes
  override lazy val module = new LazyModuleImp(this) {

    // connect the clock and negedge reset to the default clock and reset
    ioBridgeSink.bundle.PCLK    := clock.asUInt
    ioBridgeSink.bundle.PRESETn := !(reset.toBool)


    // connect ioBridge source and sink
    ioBridgeSink.bundle.capt0_event   := ioBridgeSource.bundle.capt0_event
    ioBridgeSink.bundle.capt1_event   := ioBridgeSource.bundle.capt1_event
    ioBridgeSink.bundle.capt2_event   := ioBridgeSource.bundle.capt2_event
    ioBridgeSink.bundle.capt3_event   := ioBridgeSource.bundle.capt3_event
    ioBridgeSink.bundle.capt4_event   := ioBridgeSource.bundle.capt4_event
    ioBridgeSink.bundle.capt5_event   := ioBridgeSource.bundle.capt5_event
  }

}

object NPWMTop {
  def attach(c: NPWMTopParams)(bap: BlockAttachParams): NPWMTop = {
    val PWM = NPWMTopBase.attach(c)(bap)

    // User code here

    PWM
  }
}

class WithPWMTop extends Config(
  new WithPWMTopBase(
    //ctrl_base = 0x60000L  // pio add
    ctrl_base = 0x80000L    // PWM add
  )

    // User code here
)
