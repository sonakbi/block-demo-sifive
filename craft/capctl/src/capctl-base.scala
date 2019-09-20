// Generated Code
// Please DO NOT EDIT


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



class capctlBlackBoxIO(

) extends Bundle {
  val t_ctrl_awvalid = Input(Bool())
  val t_ctrl_awready = Output(Bool())
  val t_ctrl_awaddr = Input(UInt((32).W))
  val t_ctrl_awprot = Input(UInt((3).W))
  val t_ctrl_wvalid = Input(Bool())
  val t_ctrl_wready = Output(Bool())
  val t_ctrl_wdata = Input(UInt((32).W))
  val t_ctrl_wstrb = Input(Bool())
  val t_ctrl_bvalid = Output(Bool())
  val t_ctrl_bready = Input(Bool())
  val t_ctrl_bresp = Output(UInt((2).W))
  val t_ctrl_arvalid = Input(Bool())
  val t_ctrl_arready = Output(Bool())
  val t_ctrl_araddr = Input(UInt((32).W))
  val t_ctrl_arprot = Input(UInt((3).W))
  val t_ctrl_rvalid = Output(Bool())
  val t_ctrl_rready = Input(Bool())
  val t_ctrl_rdata = Output(UInt((32).W))
  val t_ctrl_rresp = Output(UInt((2).W))
  val odata = Output(UInt((6).W))
  val oenable = Output(UInt((6).W))
  val clk = Input(Bool())
  val reset_n = Input(Bool())
}

class capctl(

) extends BlackBox(Map(

)) with HasBlackBoxResource {
  val io = IO(new capctlBlackBoxIO(

  ))
// setResource("top.v")
}

case class capctlParams(
  t_ctrlParams: Pt_ctrlParams,
  cacheBlockBytes: Int
)


class t_ctrlBundle() extends Bundle {
  val `ACLK` = Input(Bool())
  val `ARESETn` = Input(Bool())
  val `ARPROT` = Input(UInt((3).W))
  val `BREADY` = Input(Bool())
  val `BVALID` = Output(Bool())
  val `BRESP` = Output(UInt((2).W))
  val `WREADY` = Output(Bool())
  val `RRESP` = Output(UInt((2).W))
  val `ARVALID` = Input(Bool())
  val `ARREADY` = Output(Bool())
  val `AWPROT` = Input(UInt((3).W))
  val `RREADY` = Input(Bool())
  val `WSTRB` = Input(Bool())
  val `RVALID` = Output(Bool())
  val `AWADDR` = Input(UInt((32).W))
  val `WVALID` = Input(Bool())
  val `RDATA` = Output(UInt((32).W))
  val `ARADDR` = Input(UInt((32).W))
  val `AWREADY` = Output(Bool())
  val `WDATA` = Input(UInt((32).W))
  val `AWVALID` = Input(Bool())
}

class LcapctlBase(c: capctlParams)(implicit p: Parameters) extends LazyModule {
  val device = new SimpleDevice("capctl", Seq("sifive,capctl-v0"))



  val t_ctrlNode = BundleBridgeSource(() => new t_ctrlBundle)

  val ioBridgeSource = BundleBridgeSource(() => new capctlBlackBoxIO(

  ))

  class LcapctlBaseImp extends LazyRawModuleImp(this) {
    val blackbox = Module(new capctl(

    ))
    // interface wiring 2
    // busType: AXI4-Lite, mode: slave
    // port wiring
    blackbox.io.t_ctrl_awvalid := ioBridgeSource.bundle.t_ctrl_awvalid
    ioBridgeSource.bundle.t_ctrl_awready := blackbox.io.t_ctrl_awready
    blackbox.io.t_ctrl_awaddr := ioBridgeSource.bundle.t_ctrl_awaddr
    blackbox.io.t_ctrl_awprot := ioBridgeSource.bundle.t_ctrl_awprot
    blackbox.io.t_ctrl_wvalid := ioBridgeSource.bundle.t_ctrl_wvalid
    ioBridgeSource.bundle.t_ctrl_wready := blackbox.io.t_ctrl_wready
    blackbox.io.t_ctrl_wdata := ioBridgeSource.bundle.t_ctrl_wdata
    blackbox.io.t_ctrl_wstrb := ioBridgeSource.bundle.t_ctrl_wstrb
    ioBridgeSource.bundle.t_ctrl_bvalid := blackbox.io.t_ctrl_bvalid
    blackbox.io.t_ctrl_bready := ioBridgeSource.bundle.t_ctrl_bready
    ioBridgeSource.bundle.t_ctrl_bresp := blackbox.io.t_ctrl_bresp
    blackbox.io.t_ctrl_arvalid := ioBridgeSource.bundle.t_ctrl_arvalid
    ioBridgeSource.bundle.t_ctrl_arready := blackbox.io.t_ctrl_arready
    blackbox.io.t_ctrl_araddr := ioBridgeSource.bundle.t_ctrl_araddr
    blackbox.io.t_ctrl_arprot := ioBridgeSource.bundle.t_ctrl_arprot
    ioBridgeSource.bundle.t_ctrl_rvalid := blackbox.io.t_ctrl_rvalid
    blackbox.io.t_ctrl_rready := ioBridgeSource.bundle.t_ctrl_rready
    ioBridgeSource.bundle.t_ctrl_rdata := blackbox.io.t_ctrl_rdata
    ioBridgeSource.bundle.t_ctrl_rresp := blackbox.io.t_ctrl_rresp
    ioBridgeSource.bundle.odata := blackbox.io.odata
    ioBridgeSource.bundle.oenable := blackbox.io.oenable
    blackbox.io.clk := ioBridgeSource.bundle.clk
    blackbox.io.reset_n := ioBridgeSource.bundle.reset_n
    // interface alias
    val t_ctrl0 = t_ctrlNode.out(0)._1
    // interface wiring
    // wiring for t_ctrl of type AXI4-Lite
    // {"ACLK":"clk","ARESETn":"reset_n","ARPROT":"t_ctrl_arprot","BREADY":"t_ctrl_bready","BVALID":"t_ctrl_bvalid","BRESP":"t_ctrl_bresp","WREADY":"t_ctrl_wready","RRESP":"t_ctrl_rresp","ARVALID":"t_ctrl_arvalid","ARREADY":"t_ctrl_arready","AWPROT":"t_ctrl_awprot","RREADY":"t_ctrl_rready","WSTRB":"t_ctrl_wstrb","RVALID":"t_ctrl_rvalid","AWADDR":"t_ctrl_awaddr","WVALID":"t_ctrl_wvalid","RDATA":"t_ctrl_rdata","ARADDR":"t_ctrl_araddr","AWREADY":"t_ctrl_awready","WDATA":"t_ctrl_wdata","AWVALID":"t_ctrl_awvalid"}
  }
  lazy val module = new LcapctlBaseImp
}

case class Pt_ctrlParams() // name: AXI4-Lite, mode: slave

case class NcapctlTopParams(
  blackbox: capctlParams
) {
  def setBurstBytes(x: Int): NcapctlTopParams = this.copy()
}

object NcapctlTopParams {
  def defaults(
    cacheBlockBytes: Int
  ) = NcapctlTopParams(
    blackbox = capctlParams(
      t_ctrlParams = Pt_ctrlParams(),
      cacheBlockBytes = cacheBlockBytes
    )
  )
}

class NcapctlTopBase(c: NcapctlTopParams)(implicit p: Parameters) extends SimpleLazyModule {
  val imp = LazyModule(new Lcapctl(c.blackbox))

// no channel node

  val t_ctrlNode = BundleBridgeSink[t_ctrlBundle]
  t_ctrlNode := imp.t_ctrlNode
}

object NcapctlTopBase {
  def attach(c: NcapctlTopParams)(bap: BlockAttachParams): NcapctlTop = {
    implicit val p: Parameters = bap.p
    val capctl_top = LazyModule(new NcapctlTop(c))
    // no channel attachment
    // busType: AXI4-Lite, mode: slave
    capctl_top
  }
}

class WithcapctlTopBase (

) extends Config((site, here, up) => {
  case BlockDescriptorKey =>
    BlockDescriptor(
      name = "capctl",
      place = NcapctlTop.attach(NcapctlTopParams.defaults(
        cacheBlockBytes = site(CacheBlockBytes)
      ))
    ) +: up(BlockDescriptorKey, site)
})
