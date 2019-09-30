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

// busType: AXI4Lite, mode: slave

class LcapctlBase(c: capctlParams)(implicit p: Parameters) extends LazyModule {
  val device = new SimpleDevice("capctl", Seq("sifive,capctl-v0"))



  val t_ctrlNode = AXI4SlaveNode(Seq(
    AXI4SlavePortParameters(
      slaves = Seq(
        AXI4SlaveParameters(
          //@tom address       = List(AddressSet(c.t_ctrlParams.base, ((1L << 32) - 1))),
          address       = List(AddressSet(c.t_ctrlParams.base, ((1L << 10) - 1))),
          executable    = c.t_ctrlParams.executable,
          supportsWrite = TransferSizes(1, (32 / 8)),
          supportsRead  = TransferSizes(1, (32 / 8)),
          interleavedId = Some(0)
        )
      ),
      beatBytes = 32 / 8
    )
  ))


  val ioBridgeSource = BundleBridgeSource(() => new capctlBlackBoxIO(

  ))

  class LcapctlBaseImp extends LazyRawModuleImp(this) {
    val blackbox = Module(new capctl(

    ))
    // interface wiring 2

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
    val t_ctrl0 = t_ctrlNode.in(0)._1
    // interface wiring
    // wiring for t_ctrl of type AXI4Lite
    // -> {"aw":{"valid":1,"ready":-1,"bits":{"id":"awIdWidth","addr":"awAddrWidth","len":8,"size":3,"burst":2,"lock":1,"cache":4,"prot":3,"qos":4}},"w":{"valid":1,"ready":-1,"bits":{"data":"wDataWidth","strb":"wStrbWidth","last":1}},"b":{"valid":-1,"ready":1,"bits":{"id":"-bIdWidth","resp":-2}},"ar":{"valid":1,"ready":-1,"bits":{"id":"arIdWidth","addr":"addrWidth","len":8,"size":3,"burst":2,"lock":1,"cache":4,"prot":3,"qos":4}},"r":{"valid":-1,"ready":1,"bits":{"id":"-rIdWidth","data":"-dataWidth","resp":-2,"last":-1}}}// aw
    blackbox.io.t_ctrl_awvalid := t_ctrl0.aw.valid
    t_ctrl0.aw.ready := blackbox.io.t_ctrl_awready
    // aw
    // AWID
    blackbox.io.t_ctrl_awaddr := t_ctrl0.aw.bits.addr
    // AWLEN
    // AWSIZE
    // AWBURST
    // AWLOCK
    // AWCACHE
    blackbox.io.t_ctrl_awprot := t_ctrl0.aw.bits.prot
    // AWQOS
    // w
    blackbox.io.t_ctrl_wvalid := t_ctrl0.w.valid
    t_ctrl0.w.ready := blackbox.io.t_ctrl_wready
    // w
    blackbox.io.t_ctrl_wdata := t_ctrl0.w.bits.data
    blackbox.io.t_ctrl_wstrb := t_ctrl0.w.bits.strb
    // WLAST
    // b
    t_ctrl0.b.valid := blackbox.io.t_ctrl_bvalid
    blackbox.io.t_ctrl_bready := t_ctrl0.b.ready
    // b
    t_ctrl0.b.bits.id := 0.U // BID
    t_ctrl0.b.bits.resp := blackbox.io.t_ctrl_bresp
    // ar
    blackbox.io.t_ctrl_arvalid := t_ctrl0.ar.valid
    t_ctrl0.ar.ready := blackbox.io.t_ctrl_arready
    // ar
    // ARID
    blackbox.io.t_ctrl_araddr := t_ctrl0.ar.bits.addr
    // ARLEN
    // ARSIZE
    // ARBURST
    // ARLOCK
    // ARCACHE
    blackbox.io.t_ctrl_arprot := t_ctrl0.ar.bits.prot
    // ARQOS
    // r
    t_ctrl0.r.valid := blackbox.io.t_ctrl_rvalid
    blackbox.io.t_ctrl_rready := t_ctrl0.r.ready
    // r
    t_ctrl0.r.bits.id := 0.U // RID
    t_ctrl0.r.bits.data := blackbox.io.t_ctrl_rdata
    t_ctrl0.r.bits.resp := blackbox.io.t_ctrl_rresp
    t_ctrl0.r.bits.last := true.B // RLAST

  }
  lazy val module = new LcapctlBaseImp
}


case class Pt_ctrlParams(
  base: BigInt,
  executable: Boolean = false,
  maxFifoBits: Int = 2,
  maxTransactions: Int = 1,
  axi4BufferParams: AXI4BufferParams = AXI4BufferParams(),
  tlBufferParams: TLBufferParams = TLBufferParams()
)


case class NcapctlTopParams(
  blackbox: capctlParams
) {
  def setBurstBytes(x: Int): NcapctlTopParams = this.copy()
}

object NcapctlTopParams {
  def defaults(
    t_ctrl_base: BigInt,
    cacheBlockBytes: Int
  ) = NcapctlTopParams(
    blackbox = capctlParams(
      t_ctrlParams = Pt_ctrlParams(base = t_ctrl_base),
      cacheBlockBytes = cacheBlockBytes
    )
  )
}

class NcapctlTopBase(c: NcapctlTopParams)(implicit p: Parameters) extends SimpleLazyModule {
  val imp = LazyModule(new Lcapctl(c.blackbox))

// no channel node

  val t_ctrlNode: AXI4SlaveNode = imp.t_ctrlNode

  def gett_ctrlNodeTLAdapter(): TLInwardNode = {(
    t_ctrlNode
      := AXI4Buffer(
        aw = c.blackbox.t_ctrlParams.axi4BufferParams.aw,
        ar = c.blackbox.t_ctrlParams.axi4BufferParams.ar,
        w = c.blackbox.t_ctrlParams.axi4BufferParams.w,
        r = c.blackbox.t_ctrlParams.axi4BufferParams.r,
        b = c.blackbox.t_ctrlParams.axi4BufferParams.b
      )
      := AXI4UserYanker(capMaxFlight = Some(c.blackbox.t_ctrlParams.maxTransactions))
      := TLToAXI4()
      := TLFragmenter((32 / 8), c.blackbox.cacheBlockBytes, holdFirstDeny=true)
      := TLBuffer(
        a = c.blackbox.t_ctrlParams.tlBufferParams.a,
        b = c.blackbox.t_ctrlParams.tlBufferParams.b,
        c = c.blackbox.t_ctrlParams.tlBufferParams.c,
        d = c.blackbox.t_ctrlParams.tlBufferParams.d,
        e = c.blackbox.t_ctrlParams.tlBufferParams.e
      )
  )}

}

object NcapctlTopBase {
  def attach(c: NcapctlTopParams)(bap: BlockAttachParams): NcapctlTop = {
    implicit val p: Parameters = bap.p
    val capctl_top = LazyModule(new NcapctlTop(c))
    // no channel attachment
    bap.pbus.coupleTo("axi") { capctl_top.gett_ctrlNodeTLAdapter() := TLWidthWidget(bap.pbus) := _ }
    capctl_top
  }
}

class WithcapctlTopBase (
  t_ctrl_base: BigInt
) extends Config((site, here, up) => {
  case BlockDescriptorKey =>
    BlockDescriptor(
      name = "capctl",
      place = NcapctlTop.attach(NcapctlTopParams.defaults(
        t_ctrl_base = t_ctrl_base,
        cacheBlockBytes = site(CacheBlockBytes)
      ))
    ) +: up(BlockDescriptorKey, site)
})
