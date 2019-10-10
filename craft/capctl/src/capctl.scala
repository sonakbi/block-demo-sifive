// USER editable file


package sifive.blocks.capctl

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

import sifive.blocks.PWM._

class Lcapctl(c: capctlParams)(implicit p: Parameters) extends LcapctlBase(c)(p)
{

// User code here

}

class NcapctlTop(c: NcapctlTopParams)(implicit p: Parameters) extends NcapctlTopBase(c)(p)
{

// User code here
  // route the ports of the black box to this sink
  val ioBridgeSink = BundleBridgeSink[capctlBlackBoxIO]()
  ioBridgeSink := imp.ioBridgeSource

  // create a new ports for odata
  val pwmIO = BundleBridgeSink[NPWMTopIO]

  // logic to connect ioBridgeSink/Source nodes
  override lazy val module = new LazyModuleImp(this) {

    // connect the clock and negedge reset to the default clock and reset
    ioBridgeSink.bundle.clk     := clock.asUInt
    ioBridgeSink.bundle.reset_n := !(reset.toBool)

    // connect ioBridge source and sink

    pwmIO.bundle.capt0_event := ioBridgeSink.bundle.odata(0)
    pwmIO.bundle.capt1_event := ioBridgeSink.bundle.odata(1)
    pwmIO.bundle.capt2_event := ioBridgeSink.bundle.odata(2)
    pwmIO.bundle.capt3_event := ioBridgeSink.bundle.odata(3)
    pwmIO.bundle.capt4_event := ioBridgeSink.bundle.odata(4)
    pwmIO.bundle.capt5_event := ioBridgeSink.bundle.odata(5)
  }

}

object NcapctlTop {
  def attach(c: NcapctlTopParams)(bap: BlockAttachParams): NcapctlTop = {
    val capctl = NcapctlTopBase.attach(c)(bap)
    val pwm = NPWMTopBase.attach(NPWMTopParams.defaults(0x70000L, c.blackbox.cacheBlockBytes))(bap)

    implicit val p: Parameters = bap.p

    // connect the PWM and capctl signals
    capctl.pwmIO := pwm.ioBridgeSource

    capctl
  }
}

class WithcapctlTop extends Config(
  new WithcapctlTopBase(
    t_ctrl_base = 0x40000L
  )

    // User code here
)
