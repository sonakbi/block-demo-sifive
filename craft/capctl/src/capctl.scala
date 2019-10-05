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

// @tom add start
//import sifive.blocks.PWM._
import sifive.blocks._

class NcapctlTopIO(
) extends Bundle {
  val odata = Output(UInt((6).W))
}
// end

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

// @tom add start
  // create a new ports for odata
  val ioBridgeSource = BundleBridgeSource(() => new NcapctlTopIO())
// end

  // logic to connect ioBridgeSink/Source nodes
  override lazy val module = new LazyModuleImp(this) {

    // connect the clock and negedge reset to the default clock and reset
    ioBridgeSink.bundle.clk     := clock.asUInt
    ioBridgeSink.bundle.reset_n := !(reset.toBool)
  }

// @tom add start
    // connect ioBridge source and sink
    ioBridgeSource.bundle.odata   := ioBridgeSink.bundle.odata
// end

}

object NcapctlTop {
  def attach(c: NcapctlTopParams)(bap: BlockAttachParams): NcapctlTop = {
    val capctl = NcapctlTopBase.attach(c)(bap)

    // User code here
    implicit val p: Parameters = bap.p

// @tom add start
    // @tom add instantiate and connect the PWM in modules
    // bap.testHarness 
    {
      // instantiate the PWM vip
      val PWMP = NPWMTopParams(
        blackbox = PWMParams(
          //capctlWidth = c.blackbox.capctlWidth,
          cacheBlockBytes = p(CacheBlockBytes)))
      val PWM = NPWMTop.attach(PWMP)(bap)

      // route PWM signals to the testharness
      val PWMNode = BundleBridgeSink[PWMBlackBoxIO]()
      PWMNode := PWM.imp.ioBridgeSource

      // route capctl signals to the testharness
      val capctlNode = BundleBridgeSink[NcapctlTocapctl]()
      capctlNode := capctl.ioBridgeSource

      // connect the capctl and PWM signals
      InModuleBody {
        PWMNode.bundle.odata   := capctlNode.bundle.odata
      }
    }
// end
    capctl
  }
}

class WithcapctlTop extends Config(
  new WithcapctlTopBase(
    t_ctrl_base = 0x40000L
  )

    // User code here
)
