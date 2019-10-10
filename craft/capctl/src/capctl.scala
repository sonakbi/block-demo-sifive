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

//@tom
class NcapctlTopIO(
) extends Bundle {
  val odata = Output(UInt((6).W))
}
//

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
  val ioBridgeSource = BundleBridgeSource(() => new NcapctlTopIO())

  // logic to connect ioBridgeSink/Source nodes
  override lazy val module = new LazyModuleImp(this) {

    // connect the clock and negedge reset to the default clock and reset
    ioBridgeSink.bundle.clk     := clock.asUInt
    ioBridgeSink.bundle.reset_n := !(reset.toBool)

    // connect ioBridge source and sink
    ioBridgeSource.bundle.odata   := ioBridgeSink.bundle.odata
  }

}

object NcapctlTop {
  def attach(c: NcapctlTopParams)(bap: BlockAttachParams): NcapctlTop = {
    val capctl = NcapctlTopBase.attach(c)(bap)

    // User code here
    implicit val p: Parameters = bap.p

//------------------------------------------------------------------
    // instantiate and connect the capctl
    // bap.testHarness {
    {

      // route capctl signals to the testharness
      val capctlNode = BundleBridgeSink[NcapctlTopIO]()
      capctlNode := capctl.ioBridgeSource

      // route PWM signals to the testharness
      // val PWMNode = BundleBridgeSink[NPWMTopIO]()
      // PWMNode := NPWMTop.PWM.ioBridgeSource

      // connect the PWM and capctl signals
      InModuleBody {
          // PWMNode.bundle.capt0_event     := capctlNode.bundle.odata(0) 
          // PWMNode.bundle.capt1_event     := capctlNode.bundle.odata(1) 
          // PWMNode.bundle.capt2_event     := capctlNode.bundle.odata(2) 
          // PWMNode.bundle.capt3_event     := capctlNode.bundle.odata(3) 
          // PWMNode.bundle.capt4_event     := capctlNode.bundle.odata(4) 
          // PWMNode.bundle.capt5_event     := capctlNode.bundle.odata(5) 
      }
    }

//------------------------------------------------------------------

    capctl
  }
}

class WithcapctlTop extends Config(
  new WithcapctlTopBase(
    t_ctrl_base = 0x40000L
  )

    // User code here
)
